FROM eclipse-temurin:17-jre
WORKDIR /ruoyi/server
ENV TZ=Asia/Shanghai
COPY ruoyi-admin/target/ruoyi-admin.jar app.jar
EXPOSE 18080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar","--spring.profiles.active=prod"]
