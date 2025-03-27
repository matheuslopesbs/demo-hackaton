

### **Visão Geral do Projeto**

O projeto consiste em um sistema que processa vídeos e utiliza uma arquitetura de microsserviços, com integração via Kafka para comunicação assíncrona entre os componentes. O processamento envolve manipulação de vídeos (usando o FFmpeg) e o resultado é gerado como um arquivo ZIP contendo imagens extraídas do vídeo.

Os principais componentes são:

1. **API de Processamento de Vídeos**: Um microsserviço que recebe solicitações para processar vídeos.
2. **Kafka**: Usado para enviar e consumir mensagens relacionadas ao processamento de vídeos.
3. **PostgreSQL**: Utilizado como banco de dados para armazenar informações relacionadas.
4. **FFmpeg**: Utilizado para processar os vídeos, extraindo frames e gerando arquivos ZIP.
5. **Docker e Docker Compose**: Usados para gerenciar a infraestrutura e os serviços necessários para rodar o sistema, como Kafka, PostgreSQL e os microsserviços.

### **Funcionamento**

1. **Envio da Solicitação de Processamento**:
   Quando um cliente envia uma requisição para o microsserviço de processamento de vídeos (por exemplo, via uma API REST), um vídeo é identificado para processamento. O serviço envia uma **mensagem para o Kafka** com o ID do vídeo, solicitando que o vídeo seja processado. Isso é feito através de um **producer Kafka** que envia a mensagem para o **tópico Kafka** relevante (neste caso, o tópico `video-process-topic`).

   Aqui está um exemplo de log:

   ```
   2025-03-26 22:11:06 Mensagem enviada ao Kafka para processar o vídeo: 2a064b6b-af7e-4830-a993-0211a8d20c72
   ```

2. **Processamento Assíncrono via Kafka**:
   O Kafka atua como um intermediário entre o serviço que solicita o processamento e o serviço que efetivamente processa o vídeo. Quando a mensagem é publicada no tópico `video-process-topic`, um **consumer Kafka** fica responsável por escutar esse tópico. No seu caso, você criou o consumer dentro da classe `KafkaVideoConsumer`, que é anotada com `@KafkaListener`.

   Esse **listener** consome a mensagem e chama o serviço de processamento de vídeo, passando o ID do vídeo. O processamento do vídeo ocorre de forma assíncrona, ou seja, o cliente que fez a solicitação original não precisa esperar o término do processamento.

   Exemplo de log quando a mensagem é recebida pelo consumer:

   ```
   2025-03-26 22:11:07 Mensagem recebida para processar o vídeo: 2a064b6b-af7e-4830-a993-0211a8d20c72
   ```

3. **Processamento do Vídeo**:
   O serviço de processamento (`VideoProcessingService`) recebe o caminho do vídeo e usa o **FFmpeg** para processar o vídeo (extrair frames e salvar imagens). O processamento pode levar algum tempo, mas como o Kafka é baseado em mensagens assíncronas, esse tempo não afeta o fluxo principal da aplicação.

   Após o processamento, as imagens extraídas são salvas em um diretório, e um arquivo ZIP é gerado contendo essas imagens.

   Exemplo de log indicando que o processamento foi concluído:

   ```
   2025-03-26 22:11:09 Processamento concluído e arquivo zip gerado: /videos/output/2a064b6b-af7e-4830-a993-0211a8d20c72.zip
   ```



### **Kafka no Processo**

Kafka é o coração da comunicação assíncrona entre os serviços no seu projeto. Aqui estão os pontos chave:

- **Producer**: O microsserviço de API ou qualquer outro serviço relevante atua como **producer** ao enviar uma mensagem para o Kafka quando o processamento de um vídeo é solicitado.

- **Topic**: As mensagens são enviadas para o **tópico** Kafka (`video-process-topic`). Cada mensagem contém informações sobre o vídeo que precisa ser processado.

- **Consumer**: O **consumer** (`KafkaVideoConsumer`) fica escutando o tópico. Assim que ele recebe uma mensagem, o processo de extração de frames do vídeo é iniciado.

Essa abordagem assíncrona é ideal para processamentos que podem ser demorados, como o processamento de vídeos, pois evita que o cliente fique aguardando por um longo período e garante que o serviço de processamento possa operar de maneira eficiente e escalável.

### **Pontos Adicionais**

- **Kafka facilita a escalabilidade**: Se o volume de vídeos crescer, é possível adicionar mais consumidores para processar os vídeos em paralelo.

- **Mensagens persistentes**: As mensagens enviadas ao Kafka são persistentes, o que significa que mesmo se o serviço de processamento de vídeo estiver temporariamente indisponível, as mensagens serão processadas assim que o serviço estiver novamente disponível.

Essa arquitetura garante que seu projeto seja flexível, escalável e eficiente no processamento de vídeos.
