/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hzero.starter.sqlparser.sql.ast.statement;

import org.hzero.starter.sqlparser.sql.ast.SQLExpr;
import org.hzero.starter.sqlparser.sql.ast.SQLObject;
import org.hzero.starter.sqlparser.sql.ast.SQLStatementImpl;
import org.hzero.starter.sqlparser.sql.visitor.SQLASTVisitor;

import java.util.Collections;
import java.util.List;

public class SQLReturnStatement extends SQLStatementImpl {

    private SQLExpr expr;

    public SQLReturnStatement() {

    }

    public SQLReturnStatement(String dbType) {
        super (dbType);
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
        }
        visitor.endVisit(this);
    }

    public SQLReturnStatement clone() {
        SQLReturnStatement x = new SQLReturnStatement();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.expr);
    }
}
