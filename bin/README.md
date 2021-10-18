# GTrack-ProcessingNode
Sistema de acompanhamento das posições de entidades de Gado com ContextNet

## Setup
Para executar:

1. Login no registry da imagem do ContextNet Kafka
```
make login-docker
```

2. Provisionar ambiente do gateway:
```
make environment
```

3) Execute o Processing Node para monitorar as mensagens:
```
java -jar MainPN.jar
```