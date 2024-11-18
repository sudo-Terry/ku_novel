package com.example.ku_novel;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

public class SQLiteDialect extends H2Dialect {
    public SQLiteDialect() {
        super();
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.BOOLEAN, "integer");
        registerColumnType(Types.DATE, "text");
        registerColumnType(Types.TIMESTAMP, "text");
    }
}
