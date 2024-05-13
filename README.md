# Projeto AWS With DynamoDB & Java Spring Data | Phase: Back-End

## API-Rest Costumer

| Test Coverage | Language |
|:-------------:|:--------:|
|![Coverage](.github/badges/jacoco.svg)|![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)|

## 🚀💻 Technologies & Tools

![JavaScript](https://img.shields.io/badge/-JavaScript-black?style=flat-square&logo=javascript) ![Angular](https://img.shields.io/badge/-Angular-red?style=flat-square&logo=angular) ![HTML5](https://img.shields.io/badge/-HTML5-E34F26?style=flat-square&logo=html5&logoColor=white)  ![CSS3](https://img.shields.io/badge/-CSS3-1572B6?style=flat-square&logo=css3)  ![Git](https://img.shields.io/badge/-Git-black?style=flat-square&logo=git)  ![GitHub](https://img.shields.io/badge/-GitHub-181717?style=flat-square&logo=github)
![Heroku](https://img.shields.io/badge/-Heroku-430098?style=flat-square&logo=heroku) ![Docker](https://img.shields.io/badge/-Docker-black?style=flat-square&logo=docker) ![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-gold?style=flat-square&logo=postgresql) ![TypeScript](https://img.shields.io/badge/-TypeScript-black?style=flat-square&logo=typescript) ![Java](https://img.shields.io/badge/-Java-red?style=flat-square&logo=java)

## :pencil: Configurando Ambiente - Back-End

| Tool                | Link                                                                                             |
|---------------------|:-------------------------------------------------------------------------------------------------|
| Java                | [https://www.java.com/pt-BR/](https://www.java.com/pt-BR/)                                       |
| InteliJ Community   | [https://www.jetbrains.com/pt-br/idea/](https://www.jetbrains.com/pt-br/idea/)                   |
| Spring              | [https://spring.io/](https://spring.io/)                                                         |
| DynamoDB LocalStack | [https://hub.docker.com/r/localstack/localstack](https://hub.docker.com/r/localstack/localstack) |
| NoSQL Workbech      | [AWS NoSQL Workbench](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html)                                                  |
| Insomnia           | [https://insomnia.rest/](https://insomnia.rest/)                                   |

### 💻DynamoDB
```json
{
  "TableName": "Costumer",
  "AttributeDefinitions": [
    {
      "AttributeName": "id",
      "AttributeType": "S"
    }
  ],
  "KeySchema": [
    {
      "AttributeName": "id",
      "KeyType": "HASH"
    }
  ],
  "ProvisionedThroughput": {
    "ReadCapacityUnits": 5,
    "WriteCapacityUnits": 5
  }
}
```
### :coffee:Desafio Futuro

Publicar a aplicação no github com gitActions

## :pencil:Comentários sobre o projeto

Esse projeto foi baseado no documento publicado pelo Kaike Ventura
[![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/kaike-ventura-185695aa/)

[![Linkedin Badge](https://img.shields.io/badge/-flaviohnm-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/flaviohnm/)](https://www.linkedin.com/in/flaviohnm/)   [![Gmail Badge](https://img.shields.io/badge/-flaviohnm@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:flaviohnm@gmail.com)](mailto:flaviohnm@gmail.com)