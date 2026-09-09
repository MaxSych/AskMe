Markdown
<div align="center">

# 💬 AnonQuestions

**An anonymous Q&A platform built with Spring Boot, Thymeleaf, and PostgreSQL.**

![Java](https://img.shields.io/badge/JAVA_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SPRING_BOOT_3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/SPRING_SECURITY-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/POSTGRESQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/THYMELEAF-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

</div>

---

## 📌 Overview

**AnonQuestions** is an Ask.fm-inspired web service where registered users receive questions on their personal public page, and guests or other users can ask questions completely anonymously.

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
| **Database** | PostgreSQL, Hibernate ORM |
| **Templating** | Thymeleaf |
| **Frontend** | HTML5, CSS3, Modern JavaScript (ES6+) |
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
Configure the Database:
Create a database in PostgreSQL:
SQL
CREATE DATABASE anon_questions;
Update src/main/resources/application.properties with your database credentials:
Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/anon_questions
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.jpa.hibernate.ddl-auto=update
Build and Run:
Bash
mvn clean install
mvn spring-boot:run
Open the Application:
Navigate to http://localhost:8081 in your browser.
