package com.example.ku_novel;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;

import java.sql.Types;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super();
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.BOOLEAN, "integer");
        registerColumnType(Types.DATE, "text");
        registerColumnType(Types.TIMESTAMP, "text");
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    @Override
    public String getAddColumnString() {
        // SQLite에서 'ALTER TABLE'로 컬럼을 추가하려면 이 작업을 지원해야 함
        return "ADD COLUMN";
    }
}