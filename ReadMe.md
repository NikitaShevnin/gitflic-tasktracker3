# Трекер задач  

Простой трекер задач с REST API.  

## Запуск через Docker Compose  

Проект можно запустить одной командой:  

```bash  
docker compose up --build  
```  

поднимается 2 контейнера один под psql второй под api

api будет доступно на порту `8080`, PostgreSQL — на `5432`.  

## Тестирование эндпоинтов API через Postman  

1. **Регистрация пользователя**  
   для регистрации мы отправляем `POST` запрос на `http://localhost:8080/api/auth/register`  
   
   в теле запроса пишем следующее

   ```json  
   {  
     "username": "user",  
     "password": "password"  
   }  
   ```  

   далее получаем ответ сервера об успешной регистрации

2. **Авторизация**  

   Необходимо отправить `POST` запрос на `http://localhost:8080/api/auth/login`

   далее нам нужно поставить заголовок в вкладке headers
      в поле key: Content-Type
      в поле value: application/json

   после этого переходим во вкладку body
      выбрать тип raw
      в выпадающем списке справа выбрать JSON
      ввести тело запроса по формату:

   {
      "username": "user",
      "password": "password"
   }

   далее получаем ответ сервера 200 OK

   для всех дальнейших запросов необходимо передавать заголовок Authorization: Basic Auth


3. **Создание задачи**  
   для создания новой задачи отправляем `POST` запрос на `http://localhost:8080/api/tasks`  

   Обязательно добавить заголовки Authorization Type: Basic Auth в соответствующей вкладке postman
   пример кредов:
   Username: user
   Password: passwors

   Тело (raw) запроса:

   ```json  
   {  
     "title": "Моя новая таска",  
     "description": "Описание",  
     "assignees": [1]  
   }  
   ```  

4. **Список задач**  
   Что бы посмотреть список задач достаточно просто отправить `GET` запрос на  `http://localhost:8080/api/tasks` и в ответе вернётся список всех задач.

5. **Получение задачи по ID**  
   `GET http://localhost:8080/api/tasks/{id}`  

6. **Обновление задачи**  
   `PUT http://localhost:8080/api/tasks/{id}` (тело как при создании)  

7. **Удаление задачи**  
   `DELETE http://localhost:8080/api/tasks/{id}`  

8. **Изменение статуса задачи**  
   `POST http://localhost:8080/api/tasks/{id}/status?status=IN_PROGRESS`  
