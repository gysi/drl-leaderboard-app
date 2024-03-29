FROM eclipse-temurin:21-jre-jammy
RUN mkdir /opt/app
COPY app.jar /opt/app
ENV JAVA_TOOL_OPTIONS="\
    -Xms300M \
    -Xmx1500M \
    -XX:MaxMetaspaceSize=200M \
    -XX:+UseStringDeduplication \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/var/log/heapdumps_backend/ \
    -XX:+UseG1GC \
    -Xlog:gc*=info:file=/var/log/gc_backend/gc_%t.log:tags,time,uptime,level:filecount=10,filesize=100M \
"
CMD ["java", "-jar", "/opt/app/app.jar"]
