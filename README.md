
# 🧙‍♂️ Módulo de Personagens – Solo RPG (Java Spring Boot)

Este é o serviço responsável por gerenciar os **personagens** em seu sistema de RPG de mesa distribuído. Ele atua como o ponto central para a criação, busca, atualização e exclusão de personagens, vinculando-os a seus proprietários (jogadores) e às suas respectivas **fichas de personagem**, que são gerenciadas por um serviço Python separado.

---

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data MongoDB**
- **MongoDB Atlas** (ou instância local)
- **Lombok**
- **Maven**

---

## 🧠 Funcionalidades

- Criar personagens com um ID único.
- Associar personagens a um `ownerId` (usuário/jogador).
- Vincular personagens a uma `fichaId`, gerenciada por outro serviço (Python).
- Buscar, listar, atualizar e deletar personagens.
- Atua como entidade de alto nível no ecossistema Solo RPG, delegando os detalhes da ficha a outro microserviço.

---

## 📦 Estrutura do Projeto

```
crudCharacters/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/solo/rpg/characterservice/
│   │   │       ├── CharacterServiceApplication.java
│   │   │       ├── controller/
│   │   │       │   └── PersonagemController.java
│   │   │       ├── model/
│   │   │       │   ├── Personagem.java
│   │   │       │   └── PersonagemCreateRequest.java
│   │   │       ├── repository/
│   │   │       │   └── PersonagemRepository.java
│   │   │       └── service/
│   │   │           └── PersonagemService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/solo/rpg/characterservice/
│               └── CrudCharactersApplicationTests.java
├── pom.xml
└── .gitignore
```

---

## ⚙️ Como Rodar Localmente

### ✅ Pré-requisitos

- Java JDK 17+
- Apache Maven 3.6+
- Instância do MongoDB (local ou Atlas)
- Serviço de Templates (Python) na porta `8000`
- Serviço de Fichas (Python) na porta `8001`

### 1. Clonar o Repositório

```bash
git clone https://github.com/Solo-RPG/crud-characters.git
cd crud-characters
```

### 2. Configurar o `application.properties`

Abra o arquivo `src/main/resources/application.properties` e edite as seguintes propriedades:

```properties
spring.data.mongodb.uri=mongodb+srv://<usuario>:<senha>@<cluster>.mongodb.net/<banco>?retryWrites=true&w=majority
spring.data.mongodb.database=solo_rpg
server.port=8002
```

> Substitua `<usuario>`, `<senha>`, `<cluster>` e `<banco>` pelos seus dados reais.

### 3. Rodar a Aplicação

```bash
mvn spring-boot:run
```

Acesse: [http://localhost:8002](http://localhost:8002)

---

## 🔗 Endpoints da API

Acesse a documentação Swagger: [http://localhost:8002/swagger-ui.html](http://localhost:8002/swagger-ui.html)

### 👥 Personagens

| Método   | Endpoint                                   | Descrição                          |
|----------|--------------------------------------------|------------------------------------|
| `POST`   | `/api/characters`                          | Cria um novo personagem            |
| `GET`    | `/api/characters`                          | Lista todos os personagens         |
| `GET`    | `/api/characters/{id}`                     | Busca personagem por ID            |
| `GET`    | `/api/characters/by-owner/{ownerId}`       | Lista personagens por usuário      |
| `PUT`    | `/api/characters/{id}`                     | Atualiza personagem                |
| `DELETE` | `/api/characters/{id}`                     | Deleta personagem por ID           |

---

## 📐 Modelo de Dados: `Personagem`

```java
public class Personagem {
    private String id;              // ID único do personagem
    private String ownerId;         // ID do jogador/dono
    private String nomePersonagem;  // Nome do personagem
    private String fichaId;         // ID da ficha (serviço Python)
}
```

---

## 🧱 Arquitetura e Integrações

Este serviço é parte do ecossistema distribuído do Solo RPG, e se comunica com:

- **Serviço de Fichas (Python)**: responsável pelos atributos técnicos da ficha.
- **Serviço de Templates (Python)**: modelos de ficha disponíveis.

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para sugerir melhorias ou relatar bugs, abra uma [issue](https://github.com/Solo-RPG/crud-characters/issues) ou envie um pull request.

---

## 📄 Licença

Distribuído sob a [Licença MIT](https://opensource.org/licenses/MIT).  
Sinta-se à vontade para usar, modificar e compartilhar!
