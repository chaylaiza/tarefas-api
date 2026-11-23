# Estágio 1: Maven compila e gera o JAR
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copia só o pom primeiro para aproveitar o cache de camadas
COPY pom.xml .
# Baixa as dependências
RUN mvn -q dependency:go-offline
COPY src ./src
# Gera target/tarefas-api.jar
RUN mvn -q package -DskipTests

# Estágio 2: imagem final leve, só com JRE + JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/tarefas-api.jar app.jar
EXPOSE 8080
# Sem segredos: tudo vem de variáveis de ambiente
ENTRYPOINT ["java", "-jar", "app.jar"]
