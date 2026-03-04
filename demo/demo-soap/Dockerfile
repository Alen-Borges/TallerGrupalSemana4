# Etapa de construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias (incluye plugins y wsdl4j)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente, incluidos los recursos XML (usuarios.xsd)
COPY src ./src

# Al compilar, el jaxb2-maven-plugin generará los modelos basados en usuarios.xsd
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto definido en application.properties
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
