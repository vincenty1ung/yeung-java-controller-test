FROM kdvolder/jdk8
#镜像的制作人
MAINTAINER uncleyeung/uncle.yeung.bo@gmail.com
VOLUME /tmp
ARG JAR_FILE
ADD $JAR_FILE /app.jar
#RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENV LANG C.UTF-8
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
