# Курсовой проект по первым четырем модулям обучения

### ПО для работы с проектом
```
* Git;
* Google Chrome;
* IntelliJ IDEA 2022.1 (Community Edition);
* JDK;
* Docker Desktop.
```

### Установка
```
1. Клонируем проект с GitHub https://github.com/Vaderiana/final/;
2. Открываем проект в IDE Intellij IDEA;
3. Запускаем Docker Desktop;
4. Выполняем в терминале команду "docker compose up" и ждем успешного запуска контейнеров;
5. Выполняем в терминале команду "java -jar artifacts/aqa-shop.jar" и ждем успешного запуска приложения
6. Для запуска тестов перед запуском проекта экспортируем креды для базы данных
export DB_URL=jdbc:mysql://localhost:3306/app
export DB_USER=***
export DB_PASS=****
```