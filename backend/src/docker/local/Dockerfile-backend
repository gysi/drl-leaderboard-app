FROM eclipse-temurin:17-jdk-alpine as backend-builder
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src/main ./src/main
COPY src/test ./src/test
RUN ./mvnw install spring-boot:repackage

FROM eclipse-temurin:17-jre-jammy
RUN mkdir /opt/app /var/log/backend /var/log/backend/gc
COPY --from=backend-builder /app/target/*.jar /opt/app/app.jar
ENV JAVA_TOOL_OPTIONS="\
    -Xms300m \
    -Xmx550m \
    -XX:+UseStringDeduplication \
    -XX:MaxMetaspaceSize=150m \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/var/log/heapdumps_backend/ \
    -XX:+UseG1GC \
    -Xlog:gc*=info:file=/var/log/gc_backend/gc_%t.log:tags,time,uptime,level:filecount=10,filesize=100M \
"

CMD [ "java", "-jar", "/opt/app/app.jar" ]
