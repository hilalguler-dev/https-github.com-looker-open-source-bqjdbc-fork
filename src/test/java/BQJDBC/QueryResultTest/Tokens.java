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
 */
package BQJDBC.QueryResultTest;

public enum Tokens {
    EXPRESSION("EXPRESSION"),
    FROMEXPRESSION("FROMEXPRESSION"),
    SELECTSTATEMENT("SELECTSTATEMENT"),
    SUBQUERY("SUBQUERY"),
    SOURCETABLE("SOURCETABLE"),
    MULTIPLECALL("MULTIPLECALL"),
    ALIAS("ALIAS"),
    TEXT("TEXT"),
    TABLENAME("TABLENAME"),
    DATASETNAME("DATASETNAME"),
    PROJECTNAME("PROJECTNAME"),
    COLUMN("COLUMN"),
    JOKERCALL("*"),
    ORDERBYEXPRESSION("ORDERBYEXPRESSION"),
    FUNCTIONCALL("FUNCTIONCALL"),
    NAME("NAME"),
    WHEREEXPRESSION("WHEREEXPRESSION"),
    BOOLEANEXPRESSION("BOOLEANEXPRESSION"),
    HAVINGEXPRESSION("HAVINGEXPRESSION"),
    FUNCTIONPARAMETERS("FUNCTIONPARAMETERS"),
    INTEGERPARAM("INTEGERPARAM"),
    STRINGLIT("STRINGLIT"),
    ONCLAUSE("ONCLAUSE"),
    CONDITION("CONDITION"),
    JOINEXPRESSION("JOINEXPRESSION"),
    DIVIDER("DIVIDER"),
    COMPARISONOPERATOR("COMPARISONOPERATOR"),
    LOGICALOPERATOR("LOGICALOPERATOR"),
    EXPRESSIONTEXT("EXPRESSIONTEXT"),
    KEYWORD("KEYWORD"),
    SCOPE("SCOPE");

    Tokens() {

    }

    String value;

    Tokens(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        if (value == null)
            return super.toString();
        else
            return value;
    }
}

