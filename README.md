# 📁 s3-file-uploader-api

A secure REST API built with **Spring Boot 3**, leveraging **Amazon S3** for file storage and **Amazon Cognito** for authentication, following the principles of **Hexagonal Architecture**.

---

## 📌 Features

- 🔐 JWT authentication via AWS Cognito
- ☁️ Upload files to Amazon S3
- ✅ Clean and extensible project structure
- 🧪 Postman collection for testing [Here](src/main/resources/postman/s3-file-uploader-api.postman_collection.json)

---

## 🚀 Tech Stack

- ✅ Kotlin + Spring Boot 3
- ✅ Spring Security + JWT filter
- ✅ Amazon Cognito (JWT)
- ✅ Amazon S3 (File Storage)
- ✅ Hexagonal (Clean) Architecture

---

## 📦 Features

| Method | Endpoint               | Description                           | Secured |
|--------|------------------------|---------------------------------------|---------|
| POST   | `/v1/files`            | Upload a file to S3                   | ✅ JWT   |
| GET    | `/v1/files/{filename}` | Generate a signed download URL        | ✅ JWT   |

---

## 🔐 Authentication with AWS Cognito

- Users authenticate through **Amazon Cognito User Pool**.
- The application validates incoming JWT tokens using **JWKS** from the configured Cognito pool.
- All API endpoints require `Authorization: Bearer <token>`.

---

## 🧱 Clean Hexagonal Architecture

```
src/
├── adapter/controller/         # REST controllers
├── application/service/        # Use cases
├── domain/model/               # Domain models
├── domain/port/                # Interfaces (ports)
├── infrastructure/
│   ├── s3/                     # S3 integration (driven adapter)
│   ├── config/                 # AWS and custom bean configuration
│   └── security/               # Cognito JWT filter and security setup
└── Application.kt              # Main class
```

---

## ⚙️ Configuration (`application.yaml`)

```yaml
aws:
  region: YOUR_REGION
  s3:
    bucket: rp-s3-file-uploader
  cognito:
    client-id: YOUR_CLIENT_ID
    user-pool-id: sa-east-1_XXXXXXXXX
    region: YOUR_REGION
    jwk-endpoint: https://cognito-idp.sa-east-1.amazonaws.com/sa-east-1_XXXXXXXXX/.well-known/jwks.json
```

---

## ✅ Requirements

- AWS account with [Free Tier](https://aws.amazon.com/free/)
- Resources needed:
  - ✅ S3 Bucket (`rp-s3-file-uploader`)
  - ✅ Cognito User Pool (with password grant enabled)
  - ✅ IAM user with access to S3 operations
- AWS credentials configured locally:
  
```bash
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key
```

Or via `~/.aws/credentials`.

---

## 🧪 How to Test

### 🔐 Get a Cognito token (login)

```bash
curl -X POST   https://YOUR_DOMAIN.auth.sa-east-1.amazoncognito.com/oauth2/token   -H "Content-Type: application/x-www-form-urlencoded"   -d "grant_type=password&client_id=YOUR_CLIENT_ID&username=admin&password=YOUR_PASSWORD"
```

Response:
```json
{
  "access_token": "eyJraWQiOiJ...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

---

### 📤 Upload a file

```bash
curl -X POST http://localhost:8080/v1/files   -H "Authorization: Bearer <ACCESS_TOKEN>"   -F "file=@/path/to/your/file.pdf"
```

---

### 📥 Get a signed download URL

```bash
curl -X GET http://localhost:8080/v1/files/filename.pdf   -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

### 📦 Postman

- Login to your Cognito User Pool and get a JWT token.

![](/src/main/resources/img/login_cognito.png)

- Upload a file.

![](/src/main/resources/img/upload_s3.png)

![](/src/main/resources/img/dashboard_s3.png)

- Get a signed URL for downloading.

![](/src/main/resources/img/file_s3.png)

---

## 🧰 Tech Highlights

- AWS SDK v2 (fully async-ready)
- Clean Architecture principles
- Spring Boot 3 with native support for Jakarta EE 10
- Secure with stateless JWT authentication

---

## 📌 Notes

- The generated download URL is temporary and private (S3 object is not public).
- This project is ideal as a base for other AWS services: SQS, SNS, RDS, etc.
- Can be deployed to ECS, Lambda (via Spring Native), or Docker.

---

## 👨‍💻 Author

**Ricardo Proença**  
[GitHub](https://github.com/rpdesenvolve) · [LinkedIn](https://www.linkedin.com/in/ricardoproenca-dev/)

---

## 📄 License

Licensed under the MIT License.