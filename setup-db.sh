#!/bin/bash

echo "Настройка базы данных для kursach..."
echo "=========================================="

# Параметры подключения
DB_NAME="kursach"
DB_USER="project_role"
DB_PASSWORD="project_role"
DB_HOST="localhost"
DB_PORT="5432"
POSTGRES_USER="${POSTGRES_USER:-postgres}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD:-}"

# Проверка, запущен ли PostgreSQL
if ! pg_isready -h $DB_HOST -p $DB_PORT > /dev/null 2>&1; then
    echo "ОШИБКА: PostgreSQL не запущен!"
    echo "Пожалуйста, запустите PostgreSQL и попробуйте снова."
    exit 1
fi

echo "PostgreSQL запущен ✓"

# Проверка подключения к PostgreSQL
export PGPASSWORD="$POSTGRES_PASSWORD"
if ! psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER -c '\q' 2>/dev/null; then
    echo "Попытка подключения без пароля..."
    unset PGPASSWORD
    if ! psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER -c '\q' 2>/dev/null; then
        echo "ОШИБКА: Не удается подключиться к PostgreSQL!"
        echo "Убедитесь, что PostgreSQL запущен и доступен."
        echo "Если требуется пароль, установите переменную окружения POSTGRES_PASSWORD"
        exit 1
    fi
fi

echo "Подключение к PostgreSQL успешно ✓"

# Создание базы данных
echo "Создание базы данных $DB_NAME..."
export PGPASSWORD="$POSTGRES_PASSWORD"
psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER <<EOF
-- Создание пользователя, если не существует
DO \$\$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = '$DB_USER') THEN
        CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
        RAISE NOTICE 'Пользователь $DB_USER создан';
    ELSE
        RAISE NOTICE 'Пользователь $DB_USER уже существует';
        -- Обновляем пароль, если пользователь существует
        ALTER USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
    END IF;
END
\$\$;

-- Удаление базы данных, если она существует
DROP DATABASE IF EXISTS $DB_NAME;

-- Создание базы данных
CREATE DATABASE $DB_NAME OWNER $DB_USER;

-- Предоставление прав на базу данных
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
EOF

# Подключение к созданной базе данных для настройки схемы
psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER -d $DB_NAME <<EOF
-- Предоставление прав на схему public
GRANT ALL ON SCHEMA public TO $DB_USER;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO $DB_USER;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO $DB_USER;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO $DB_USER;

-- Убеждаемся, что пользователь может создавать объекты
ALTER SCHEMA public OWNER TO $DB_USER;
EOF

unset PGPASSWORD

if [ $? -eq 0 ]; then
    echo "База данных $DB_NAME успешно создана ✓"
    echo "Пользователь $DB_USER настроен ✓"
    echo "Права на схему public предоставлены ✓"
    echo ""
    echo "База данных готова к использованию!"
    echo "При первом запуске приложения Hibernate автоматически создаст таблицы."
else
    echo "ОШИБКА: Не удалось создать базу данных!"
    unset PGPASSWORD
    exit 1
fi

