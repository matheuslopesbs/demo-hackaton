FROM openjdk:17-jdk-slim

# Atualiza os pacotes e instala o FFmpeg
RUN apt-get update && apt-get install -y ffmpeg

# Define o volume temporário
VOLUME /tmp

# Define o nome do JAR gerado pelo Maven
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar

# Copia o JAR para a imagem com o nome app.jar
COPY ${JAR_FILE} app.jar

# Expõe a porta 8080
EXPOSE 8080

# Configuração de variáveis de ambiente para o PostgreSQL e Kafka
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
ENV SPRING_DATASOURCE_USERNAME=user
ENV SPRING_DATASOURCE_PASSWORD=password
ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
