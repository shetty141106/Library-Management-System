# LMS Frontend

Simple React frontend for the Spring Boot Library Management System.

## Run locally

Start the backend from `lms`:

```bash
./mvnw spring-boot:run
```

Start the frontend from `lms/frontend`:

```bash
npm install
npm run dev
```

The Vite dev server runs on `http://localhost:5173` and proxies `/api` requests to `http://localhost:8080`.
