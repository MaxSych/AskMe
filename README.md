Markdown
<div align="center">

# 💬 AskMe

**An anonymous Q&A platform built with Spring Boot, Thymeleaf, and PostgreSQL.**

![Java](https://img.shields.io/badge/JAVA_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SPRING_BOOT-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/SPRING_SECURITY-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/HIBERNATE-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Flyway](https://img.shields.io/badge/FLYWAY-CC0202?style=for-the-badge&logo=flyway&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/POSTGRESQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![TypeScript](https://img.shields.io/badge/TYPESCRIPT-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![JavaScript](https://img.shields.io/badge/JAVASCRIPT-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Thymeleaf](https://img.shields.io/badge/THYMELEAF-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
</div>

---

## 📌 Overview

**AskMe** is an Ask.fm-inspired web service where registered users receive questions on their personal public page, and guests or other users can ask questions completely anonymously. 
⚙️ Web version is in progress ... 

---

## ✨ Key Features

* 👤 **User Profiles:** Each user receives a dedicated public profile page.
* ❓ **Anonymous Questions:** Anyone can ask questions anonymously without exposing their identity.
* ✍️ **Q&A Threading:** Profile owners can publish answers to incoming questions.
* 🔒 **Role & Access Control:** Protected user dashboards and secure endpoints via Spring Security.

---

## 🛠️ Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Backend** | Java 17+, Spring Boot (Spring Data JPA, Spring Security) |
| **Database** | PostgreSQL, Hibernate ORM, Flyway |
| **Templating** | Thymeleaf |
| **Frontend** | HTML5, CSS3, Modern JavaScript (ES6+), Thymeleaf |
| **Build Tool** | Apache Maven |

---

## 🚀 Getting Started

### Prerequisites

* **JDK 17** or higher
* **PostgreSQL 14+**
* **Maven 3.8+** (or use the included `./mvnw`)

---

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/MaxSych/AnonQuestions.git](https://github.com/MaxSych/AnonQuestions.git)
   cd AnonQuestions
   
2. Configure the Database:
Create a database in PostgreSQL:
SQL
CREATE DATABASE anon_questions;
Update src/main/resources/application.properties with your database credentials:
 Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/anon_questions
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update

3. Build and Run:
Bash
mvn clean install
mvn spring-boot:run

4. Open the Application:
Navigate to http://localhost:8081 in your browser.
