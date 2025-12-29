# SocialMeet – Backend ⚙️

SocialMeet Backend is a RESTful API service that powers the SocialMeet social media application.  
It provides secure and scalable backend services for authentication, users, posts, and social interactions.

---

## 🛠 Tech Stack

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

---

## ✨ Features

- JWT-based Authentication & Authorization
- User Registration & Login
- User Profile Management
- Post Creation & Image Upload
- Like & Comment on Posts
- Follow / Unfollow Users
- User Search API
- Secure REST APIs

---

## ⚙️ Configuration

Create a file named .env in the root directory:

```properties
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/socialsphere
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000

# Mail (SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

```
---

🧪 Run Locally

git clone https://github.com/prmjagdish/SocialMeet-Backend.git
- cd SocialMeet-Backend
- mvn clean install
- mvn spring-boot:run

Server will run on:

http://localhost:8080


---

🐳 Run with Docker

docker build -t socialmeet-backend .
docker run -p 8080:8080 socialmeet-backend


---

🔗 Frontend Repository

https://github.com/prmjagdish/SocialMeet


---

📌 Future Enhancements

Real-time Chat (WebSocket)

Notification System

Redis Caching

Rate Limiting

Swagger / OpenAPI Documentation

Role-based Access Control



---

👨‍💻 Author

Jagdish Parmar
Backend / Full Stack Developer (Java | Spring Boot | Microservices)

⭐ Star this repository if you find it useful!
