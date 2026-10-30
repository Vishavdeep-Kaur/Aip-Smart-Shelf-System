# Library Management System

A Spring Boot + Thymeleaf library management application backed by MySQL.

## Requirements

- JDK 25 (the Maven project targets Java 25)
- Apache NetBeans 31 or newer with Maven support
- MySQL Server

## Database setup

Create the database in MySQL:

```sql
CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Configure connection settings through environment variables before running:

- `DB_URL` (defaults to `jdbc:mysql://localhost:3306/library_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC`)
- `DB_USERNAME` (defaults to `root`)
- `DB_PASSWORD` (set this to your local MySQL password)

Hibernate creates and updates tables on startup (`spring.jpa.hibernate.ddl-auto=update`).

## Open in NetBeans

Choose **File → Open Project**, select this folder, and let NetBeans load the Maven project. Select JDK 25 as the project Java Platform.

## Run

On Windows PowerShell, set the database password and start the application:

```powershell
$env:DB_PASSWORD = 'your-local-mysql-password'
.\mvnw.cmd spring-boot:run
```

Then open <http://localhost:8080>.

The app includes book catalog management, student registration, book loans and returns, and an overdue report.
