FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /workdir
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17.0.2-jdk
COPY --from=builder /workdir/target/article-*.jar app/article.jar
EXPOSE 8083
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/article.jar"]