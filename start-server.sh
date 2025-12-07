#!/bin/bash

echo "Запуск сервера приложений для проекта kursach..."
echo "================================================"

# Проверка наличия WAR файла
if [ ! -f "target/kursach.war" ]; then
    echo "WAR файл не найден. Собираю проект..."
    mvn clean package -DskipTests
fi

# Проверка PostgreSQL
if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "ОШИБКА: PostgreSQL не запущен!"
    echo "Пожалуйста, запустите PostgreSQL и попробуйте снова."
    exit 1
fi

echo "PostgreSQL запущен ✓"
echo "WAR файл готов ✓"
echo ""
echo "Для запуска приложения используйте один из следующих способов:"
echo ""
echo "1. Используйте установленный Tomcat:"
echo "   - Скопируйте target/kursach.war в директорию webapps вашего Tomcat"
echo "   - Запустите Tomcat"
echo "   - Откройте http://localhost:8080/kursach/"
echo ""
echo "2. Используйте встроенный сервер (требует установки):"
echo "   mvn jetty:run"
echo ""
echo "3. Используйте Docker (если установлен):"
echo "   docker run -d -p 8080:8080 -v \$(pwd)/target/kursach.war:/usr/local/tomcat/webapps/kursach.war tomcat:9.0"
echo ""
echo "Проект готов к деплою!"
echo "База данных: localhost:5432/kursach"
echo "Пользователь: project_role"
echo ""


