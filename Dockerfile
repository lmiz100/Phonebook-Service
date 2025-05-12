# ---------- Stage 1: Build the application ----------
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

#COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
RUN gradle --no-daemon build -x test || return 0


COPY . .
RUN gradle --no-daemon bootJar


# ---------- Stage 2: Minimal runtime image ----------
FROM eclipse-temurin:21-jre

RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring

VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]