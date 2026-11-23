# Tarefas API

API REST de tarefas feita com Spring Boot 3, Spring Data JPA e PostgreSQL, com testes automatizados, Docker, Docker Compose, logs e Actuator.
Trabalho das Aulas 10 e 11.

**Aluno:** (Chayenne Laiza da Silva - 01796436) — **Repositório:** (https://github.com/chaylaiza)

## Requisitos
- Java 21 e Maven 3.9+ (para rodar sem Docker)
- Docker e Docker Compose (para rodar em container)

## Endpoints
| Método | Rota | Descrição |
|---|---|---|
| POST | /tarefas | cadastra (201; 400 se inválida; 409 se título duplicado) |
| GET | /tarefas | lista |
| GET | /tarefas/{id} | busca por id (404 se não existe) |
| PUT | /tarefas/{id} | atualiza |
| PATCH | /tarefas/{id}/concluir | conclui (409 se já concluída) |
| DELETE | /tarefas/{id} | exclui (204; 404 se não existe) |
| GET | /actuator/health | saúde da aplicação |

Corpo de exemplo: `{"titulo":"Estudar Docker","descricao":"Aula 11","prioridade":3}` (prioridade de 1 a 5).

## 1. Rodar os testes
```bash
mvn test
```

## 2. Gerar e executar o JAR (fora da IDE)
Precisa de um PostgreSQL acessível. Exemplo com variáveis de ambiente:
```bash
mvn clean package
export DB_URL=jdbc:postgresql://localhost:5432/tarefas
export DB_USER=postgres
export DB_PASSWORD=sua_senha
java -jar target/tarefas-api.jar
```

## 3. Rodar com Docker Compose (API + PostgreSQL)
```bash
cp .env.example .env      # ajuste a senha
docker compose up --build
```
- API: http://localhost:8080/tarefas
- Saúde: http://localhost:8080/actuator/health
- Logs: `docker compose logs -f api`
- Parar (mantém dados): `docker compose down`
- Parar e apagar dados: `docker compose down -v`

Os dados do banco ficam no volume `pgdata`.

## Variáveis de ambiente
| Variável | Padrão | Uso |
|---|---|---|
| DB_URL | jdbc:postgresql://localhost:5432/tarefas | URL do banco |
| DB_USER | postgres | usuário |
| DB_PASSWORD | postgres | senha |
| LOG_LEVEL | INFO | nível de log da aplicação |
| PORT | 8080 | porta HTTP |
