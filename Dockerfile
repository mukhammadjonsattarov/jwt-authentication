# === 1. Build bosqichi ===
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Faqat pom.xml faylni oldin copy qilamiz (cache uchun)
COPY pom.xml .

# Dependencies'larni oldindan yuklab olamiz (build tezlashadi)
RUN mvn dependency:go-offline -B

# Endi src papkani copy qilamiz
COPY src ./src

# Projectni build qilamiz
RUN mvn clean package -DskipTests

# === 2. Run bosqichi ===
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Oldingi build bosqichidan JAR faylni olish
COPY --from=build /app/target/*.jar app.jar

# Port ochish (Swagger uchun 8080)
EXPOSE 8080

# Appni ishga tushirish
ENTRYPOINT ["java","-jar","app.jar"]
