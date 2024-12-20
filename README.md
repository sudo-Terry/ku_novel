## Project Structure
```angular2html
ku_novel
├── client
│     ├── connection
│     └── ui
├── common
└── server
```

- `client` : Client-Side 코드 디렉토리
  - `connection` : 서버와의 통신을 담당
  - `ui` : 클라이언트 View를 담당
- `common` : 클라이언트와 서버가 공통으로 사용하는 파일을 위한 디렉토리
- `server` : Server-Side 코드 디렉토리

## Dependencies
- SDK `Amazon corretto-17.0.13`
- GSON `v2.8.9` 
  - [MVN Repository](https://mvnrepository.com/artifact/com.google.code.gson/gson)
- sqlite-jdbc: `3.43.2.0`
  - **Installation Guide**: [SQLite JDBC Installation](https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/)
