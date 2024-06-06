# Projeto AWS With DynamoDB & Java Spring Data | Phase: Back-End

## API-Rest Costumer

|              Test Coverage              |                                                                                                                  Mutant Coverage                                                                                                                   |
|:---------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| ![Coverage](.github/badges/jacoco.svg)  | [![Mutation testing badge](https://img.shields.io/endpoint?style=flat&url=https%3A%2F%2Fbadge-api.stryker-mutator.io%2Fgithub.com%2Fflaviohnm%2Fdynamodb%2Fmain)](https://dashboard.stryker-mutator.io/reports/github.com/flaviohnm/dynamodb/main) |

## 🚀💻 Technologies & Tools

<div align="left">
  <h3>Linguagens</h3>
  <a href="https://github.com/flaviohnm?tab=repositories&language=java"><img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=OpenJDK&logoColor=white" alt="Java"/></a>
  <a href="https://github.com/flaviohnm?tab=repositories&language=java"><img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring"/></a>
  <a href="https://github.com/flaviohnm?tab=repositories&language=java"><img src="https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="ApacheMaven"/></a>

<h3>Ferramentas</h3>
<img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
<img src="https://img.shields.io/badge/Amazon%20DynamoDB-4053D6?style=for-the-badge&logo=Amazon%20DynamoDB&logoColor=white" alt="DynamoDB"/>
<img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit5"/>
<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white" alt="IntelliJ"/>
<img src="https://img.shields.io/badge/Insomnia-5849be?style=for-the-badge&logo=Insomnia&logoColor=white" alt="IntelliJ"/>
</div>

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


---
Feito por :man_astronaut: Flavio Monteiro :wave:
<br>
<a target="_blank" href="mailto:flaviohnm@gmail.com"><img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white"/></a>
<a target="_blank" href="https://www.linkedin.com/in/flaviohnm/"><img src="https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white"/></a>
<a href="https://buymeacoffee.com/flaviohnm" title="buy me a coffee" target="_blank"><img src="https://img.shields.io/badge/Buy%20Me%20a%20Coffee-ffdd00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black" align="right"></a>