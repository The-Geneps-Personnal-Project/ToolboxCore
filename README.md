# ToolboxCore

## Description

Core for toolbox project, receive request from the frontend with a restAPI and communicate with different services using grpc.

Generate grpc files from proto files

```bash
protoc --java_out=src/main/java/ -I=src/main/proto src/main/proto/\*.proto
```

Run

```bash
mvn clean install
```

Test

```bash
mvn test
```
