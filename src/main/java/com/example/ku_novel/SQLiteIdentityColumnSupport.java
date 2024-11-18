package com.example.ku_novel;

import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {

    @Override
    public boolean supportsIdentityColumns() {
        return true;  // SQLite는 Identity 컬럼을 지원합니다.
    }

    @Override
    public String getIdentityColumnString(int type) {
        // SQLite에서 사용되는 AUTO_INCREMENT를 정의합니다.
        return "";
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        // 마지막 삽입된 ID를 조회하는 쿼리
        return "select last_insert_rowid()";
    }
}
