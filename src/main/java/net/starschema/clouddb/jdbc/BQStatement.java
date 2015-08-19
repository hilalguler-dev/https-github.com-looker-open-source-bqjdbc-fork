/**
 * Copyright (c) 2015, STARSCHEMA LTD.
 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:

 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This class implements the java.sql.Statement interface
 */

package net.starschema.clouddb.jdbc;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.api.services.bigquery.model.Job;

/**
 * This class implements java.sql.Statement
 *
 * @author Horváth Attila
 * @author Balazs Gunics
 *
 */
public class BQStatement extends BQStatementRoot implements java.sql.Statement {

    /**
     * Constructor for BQStatement object just initializes local variables
     *
     * @param projectid
     * @param bqConnection
     */
    public BQStatement(String projectid, BQConnection bqConnection) {
        logger.debug("Constructor of BQStatement is running projectid is: " + projectid);
        this.ProjectId = projectid;
        this.connection = bqConnection;
        //this.resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;
        this.resultSetType = ResultSet.TYPE_FORWARD_ONLY;
        this.resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
    }

    /**
     * Constructor for BQStatement object just initializes local variables
     *
     * @param projectid2
     * @param bqConnection
     * @param resultSetType
     * @param resultSetConcurrency
     * @throws BQSQLException
     */
    public BQStatement(String projectid, BQConnection bqConnection,
                       int resultSetType, int resultSetConcurrency) throws BQSQLException {
        logger.debug("Constructor of BQStatement is running projectid is: " + projectid +
                ",resultSetType is: " + resultSetType +
                ",resutSetConcurrency is: " + resultSetConcurrency);
        if (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE) {
            throw new BQSQLException(
                    "The Resultset Concurrency can't be ResultSet.CONCUR_UPDATABLE");
        }

        this.ProjectId = projectid;
        this.connection = bqConnection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;

    }

    /** {@inheritDoc} */

    @Override
    public ResultSet executeQuery(String querySql) throws SQLException {
        if (this.isClosed()) {
            throw new BQSQLException("This Statement is Closed");
        }

        this.starttime = System.currentTimeMillis();
        Job referencedJob;
        // ANTLR Parsing
        BQQueryParser parser = new BQQueryParser(querySql, this.connection);
        querySql = parser.parse();
        try {
            // Gets the Job reference of the completed job with give Query
            referencedJob = BQSupportFuncts.startQuery(
                    this.connection.getBigquery(),
                    this.ProjectId.replace("__", ":").replace("_", "."), querySql);
            this.logger.debug("Executing Query: " + querySql);
        } catch (IOException e) {
            throw new BQSQLException("Something went wrong with the query: " + querySql, e);
        }
        try {
            do {
                if (BQSupportFuncts.getQueryState(referencedJob,
                        this.connection.getBigquery(),
                        this.ProjectId.replace("__", ":").replace("_", ".")).equals(
                        "DONE")) {
                    if (resultSetType == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                        return new BQScrollableResultSet(BQSupportFuncts.getQueryResults(
                                this.connection.getBigquery(),
                                this.ProjectId.replace("__", ":").replace("_", "."),
                                referencedJob), this);
                    } else {
                        return new BQForwardOnlyResultSet(
                                this.connection.getBigquery(),
                                this.ProjectId.replace("__", ":").replace("_", "."),
                                referencedJob, this);
                    }
                }
                // Pause execution for half second before polling job status
                // again, to
                // reduce unnecessary calls to the BigQUery API and lower
                // overall
                // application bandwidth.
                Thread.sleep(500);
                this.logger.debug("slept for 500" + "ms, querytimeout is: "
                        + this.querytimeout + "s");
            }
            while (System.currentTimeMillis() - this.starttime <= (long) this.querytimeout * 1000);
            // it runs for a minimum of 1 time
        } catch (IOException e) {
            throw new BQSQLException("Something went wrong with the query: " + querySql, e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // here we should kill/stop the running job, but bigquery doesn't
        // support that :(
        throw new BQSQLException(
                "Query run took more than the specified timeout");
    }

}
