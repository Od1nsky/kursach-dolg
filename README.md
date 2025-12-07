# Информационная подсистема учета продаж сервисов

Веб-приложение на Java EE для учета продаж сервисов, клиентов и покупок.

## Описание

Система позволяет управлять:
- **Клиентами** - информация о клиентах (имя, фамилия, email, телефон, статус)
- **Услугами** - каталог услуг с ценами
- **Покупками** - учет продаж с привязкой к клиентам и услугам

## Технологии

- **Java 17+**
- **Jakarta EE 9**
- **JSF 3.0** (Mojarra)
- **PrimeFaces 12.0**
- **Hibernate 6.0**
- **PostgreSQL**
- **CDI (Weld)**
- **Apache Tomcat 10**

## Требования

- Java 17 или выше
- Maven 3.6+
- PostgreSQL 12+
- Apache Tomcat 10 (встроен в проект)

## Установка и настройка

### 1. Настройка базы данных PostgreSQL

Создайте базу данных и пользователя:

```sql
CREATE DATABASE kursach;
CREATE USER project_role WITH PASSWORD 'project_role';
GRANT ALL PRIVILEGES ON DATABASE kursach TO project_role;
```

Или используйте существующую базу данных и обновите настройки в `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/kursach" />
<property name="jakarta.persistence.jdbc.user" value="project_role" />
<property name="jakarta.persistence.jdbc.password" value="project_role" />
```

### 2. Сборка проекта

```bash
mvn clean package
```

Это создаст WAR файл в директории `target/kursach.war`.

### 3. Запуск приложения

#### Вариант 1: Использование встроенного Tomcat (рекомендуется)

Проект уже содержит Apache Tomcat 10 в директории `apache-tomcat-10/`.

**Запуск:**

```bash
# Остановите предыдущий экземпляр (если запущен)
if [ -f tomcat10.pid ]; then kill $(cat tomcat10.pid); fi

# Скопируйте WAR файл
cp target/kursach.war apache-tomcat-10/webapps/

# Запустите Tomcat
export CATALINA_HOME=$(pwd)/apache-tomcat-10
export JAVA_HOME=$(/usr/libexec/java_home)  # Для macOS
# Для Linux: export JAVA_HOME=/usr/lib/jvm/java-17-openjdk

apache-tomcat-10/bin/catalina.sh run
```

Или в фоновом режиме:

```bash
nohup apache-tomcat-10/bin/catalina.sh run > tomcat10.log 2>&1 & echo $! > tomcat10.pid
```

**Остановка:**

```bash
apache-tomcat-10/bin/catalina.sh stop
# или
kill $(cat tomcat10.pid)
```

#### Вариант 2: Использование внешнего Tomcat

1. Скачайте и установите Apache Tomcat 10
2. Скопируйте `target/kursach.war` в директорию `webapps/` вашего Tomcat
3. Запустите Tomcat

#### Вариант 3: Использование Maven плагина

Добавьте в `pom.xml` (уже добавлен):

```xml
<plugin>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <version>11.0.20</version>
    <configuration>
        <httpConnector>
            <port>8081</port>
        </httpConnector>
        <webApp>
            <contextPath>/kursach</contextPath>
        </webApp>
    </configuration>
</plugin>
```

Запуск:

```bash
mvn jetty:run
```

## Доступ к приложению

После запуска приложение будет доступно по адресу:

- **Главная страница:** http://localhost:8081/kursach/
- **Клиенты:** http://localhost:8081/kursach/client.xhtml
- **Услуги:** http://localhost:8081/kursach/service.xhtml
- **Покупки:** http://localhost:8081/kursach/payment.xhtml
- **Вход в систему:** http://localhost:8081/kursach/login.xhtml

**Примечание:** Если порт 8081 занят, измените порт в `apache-tomcat-10/conf/server.xml` или используйте другой порт.

## Тестовые учетные данные

При первом запуске система автоматически создаст тестовые данные. Для входа используйте:

- **Администратор:** `admin` / `admin`
- **Преподаватель:** `teacher` / `teacher`
- **Студент:** `student` / `student`

## Инициализация тестовых данных

При первом обращении к главной странице автоматически загружаются тестовые данные:

- 5 клиентов
- 5 услуг
- 8 покупок

Если данные уже существуют, инициализация пропускается.

## Структура проекта

```
kursach_javaEE/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/kursach/kursach/
│   │   │       ├── config/          # Конфигурация (CDI, DataInitializer)
│   │   │       ├── controller/      # JSF контроллеры
│   │   │       ├── converter/       # JSF конвертеры
│   │   │       ├── model/           # JPA сущности
│   │   │       ├── repository/      # Репозитории
│   │   │       ├── security/        # Безопасность
│   │   │       └── service/        # Бизнес-логика
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml # Настройки JPA
│   │   └── webapp/
│   │       ├── *.xhtml             # JSF страницы
│   │       ├── WEB-INF/
│   │       │   ├── web.xml         # Конфигурация веб-приложения
│   │       │   └── templates/      # Шаблоны JSF
│   │       └── resources/          # CSS, JS
│   └── test/
│       └── java/                   # Тесты
├── apache-tomcat-10/               # Встроенный Tomcat
├── target/                         # Собранные артефакты
├── pom.xml                         # Maven конфигурация
└── README.md                       # Этот файл
```

## Модель данных

### Client (Клиент)
- `id` - идентификатор
- `firstName` - имя
- `lastName` - фамилия
- `email` - электронная почта
- `phone` - телефон
- `status` - статус

### Service (Услуга)
- `id` - идентификатор
- `name` - название услуги
- `price` - цена

### Payment (Покупка)
- `id` - идентификатор
- `client` - клиент (FK)
- `service` - услуга (FK)
- `date` - дата покупки
- `quantity` - количество
- `amount` - сумма покупки

## Разработка

### Запуск в режиме разработки

Для автоматической пересборки при изменениях используйте:

```bash
mvn clean package
# Затем перезапустите Tomcat
```

### Запуск тестов

```bash
mvn test
```

### Просмотр логов

Логи Tomcat находятся в:
- `apache-tomcat-10/logs/catalina.out`
- `tomcat10.log` (если запущен в фоне)

## Решение проблем

### Порт занят

Если порт 8081 занят, измените его в `apache-tomcat-10/conf/server.xml`:

```xml
<Connector port="8082" protocol="HTTP/1.1" ... />
```

### Ошибки подключения к БД

1. Убедитесь, что PostgreSQL запущен:
   ```bash
   pg_isready -h localhost -p 5432
   ```

2. Проверьте настройки в `persistence.xml`

3. Убедитесь, что база данных и пользователь созданы

### Ошибки при деплое

1. Проверьте логи: `apache-tomcat-10/logs/catalina.out`
2. Убедитесь, что используется Tomcat 10 (для Jakarta EE 9)
3. Проверьте версию Java (должна быть 17+)

### Ошибки конвертации дат

Используется кастомный конвертер `LocalDateConverter` для работы с `LocalDate` в JSF.

## Авторы

Курсовой проект по Java EE

## Лицензия

Учебный проект



