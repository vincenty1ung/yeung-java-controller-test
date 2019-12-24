FROM kdvolder/jdk8
#镜像的制作人
MAINTAINER uncleyeung/uncle.yeung.bo@gmail.com
VOLUME /tmp
ARG JAR_FILE
ADD $JAR_FILE app.jar
#ADD online-cashier-demo-1.0.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
