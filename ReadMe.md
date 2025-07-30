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
   `POST http://localhost:8080/api/auth/register`  
   
   ```json  
   {  
     "username": "user",  
     "password": "password"  
   }  
   ```  

2. **Авторизация**  
   Необходимо отправить `GET http://localhost:8080/api/auth/login`
   далее перейти во владку Authorization 
   выбрать в выпадающем списке Auth Type => Basic Auth
   ввести 
   - Username: логин пользователя
   - Password: пароль
   отправить запрос и получить ответ сервера.

3. **Создание задачи**  
   `POST http://localhost:8080/api/tasks`  
   ```json  
   {  
     "title": "Моя задача",  
     "description": "Описание",  
     "assignees": [1]  
   }  
   ```  

4. **Список задач**  
   `GET http://localhost:8080/api/tasks`  

5. **Получение задачи по ID**  
   `GET http://localhost:8080/api/tasks/{id}`  

6. **Обновление задачи**  
   `PUT http://localhost:8080/api/tasks/{id}` (тело как при создании)  

7. **Удаление задачи**  
   `DELETE http://localhost:8080/api/tasks/{id}`  

8. **Изменение статуса задачи**  
   `POST http://localhost:8080/api/tasks/{id}/status?status=IN_PROGRESS`  
