#!/bin/bash

PROJECT_NAME="kursach_stas"
DB_NAME="kursach_stas"
TOMCAT_DIR="apache-tomcat-10"
WAR_FILE="target/kursach.war"
PID_FILE="tomcat10.pid"

echo "=========================================="
echo "Запуск проекта: $PROJECT_NAME"
echo "=========================================="

# Проверка PostgreSQL
echo "Проверка PostgreSQL..."
if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "ОШИБКА: PostgreSQL не запущен!"
    echo "Пожалуйста, запустите PostgreSQL и попробуйте снова."
    exit 1
fi
echo "PostgreSQL запущен ✓"

# Проверка, не запущен ли уже Tomcat
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "Tomcat уже запущен (PID: $PID)"
        echo "Используйте ./stop.sh для остановки"
        exit 1
    else
        echo "Удаляю устаревший PID файл..."
        rm -f "$PID_FILE"
    fi
fi

# Сборка проекта
echo ""
echo "Сборка проекта..."
if ! mvn clean package -DskipTests; then
    echo "ОШИБКА: Не удалось собрать проект!"
    exit 1
fi
echo "Проект собран ✓"

# Проверка наличия WAR файла
if [ ! -f "$WAR_FILE" ]; then
    echo "ОШИБКА: WAR файл не найден: $WAR_FILE"
    exit 1
fi

# Копирование WAR в Tomcat
echo ""
echo "Копирование WAR в Tomcat..."
rm -rf "$TOMCAT_DIR/webapps/kursach" "$TOMCAT_DIR/webapps/kursach.war"
cp "$WAR_FILE" "$TOMCAT_DIR/webapps/"
echo "WAR файл скопирован ✓"

# Запуск Tomcat
echo ""
echo "Запуск Tomcat..."
cd "$TOMCAT_DIR"
./bin/startup.sh
cd ..

# Сохранение PID
sleep 2
if [ -f "$TOMCAT_DIR/logs/catalina.pid" ]; then
    cp "$TOMCAT_DIR/logs/catalina.pid" "$PID_FILE"
    PID=$(cat "$PID_FILE")
    echo "Tomcat запущен (PID: $PID) ✓"
else
    echo "Предупреждение: PID файл не найден, но Tomcat должен быть запущен"
fi

echo ""
echo "=========================================="
echo "Проект $PROJECT_NAME запущен!"
echo "=========================================="
echo "Приложение доступно по адресу:"
echo "  http://localhost:8080/kursach/"
echo ""
echo "Для остановки используйте: ./stop.sh"
echo "Логи Tomcat: $TOMCAT_DIR/logs/catalina.out"
echo ""
