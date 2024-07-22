# User Aggregation Service

## Description

Services for selecting data from multiple databases.

## Settings

1. Set up data sources in `application.yaml`.
2. Create the required number of databases and write their settings in  `application.yaml`.
3. Run init.sql and data.sql scripts from resources
4. For running application `./gradlew bootRun`.

## Endpoints

- `GET /users` - Возвращает список пользователей из всех баз данных.

## Sample answer

```json
[
    {
        "id": "example-user-id-1",
        "username": "user-1",
        "name": "User",
        "surname": "Userenko"
    },
    {
        "id": "example-user-id-2",
        "username": "user-2",
        "name": "Testuser",
        "surname": "Testov"
    }
]
