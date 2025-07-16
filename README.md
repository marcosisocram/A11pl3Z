<div align="center" width="100%">
    <img alt="A comet in the solar system and the names of Java 21, String boot and A11pl3Z" src="/images/logo-ai.webp?raw=true" title="Logo"/>
</div>

## Projeto Para a Rinha de Backend 3ª

### Stack

- Java 21
- Spring boot 3.5.3
- GraalVM
- HAProxy
- RabbitMQ
- PostgreSQL

## Como gerar o nativo

```shell
./mvnw -Pnative native:compile
```


## Como gerar a imagem docker

```shell
./mvnw -Pnative spring-boot:build-image
```

## Dificuldades
- Com o limite de memoria de 350MB


## Conhecimentos adquiridos

- HAProxy
- Spring Boot Native
- JOOQ
- K6
- Chaves GPG do GitHub
