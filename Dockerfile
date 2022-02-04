# Example using MS Build of OpenJDK image directly
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu

WORKDIR /app

# LABEL maintainer="swucs7@gmail.com"
ENV TZ=Asia/Seoul
# VOLUME /tmp
EXPOSE 8080
#ARG MODULE=shinyoung-wms-api
ARG JAR_FILE=target/shinyoung-wms-api.jar
ADD ${JAR_FILE} shinyoung-wms-api.jar
ENTRYPOINT ["java" \
                ,"-Djava.security.egd=file:/dev/./urandom" \
                ,"-jar" \
                ,"/app/shinyoung-wms-api.jar" \
                ,"--spring.profiles.active=real" \
            ]
