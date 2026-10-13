# MarketLink Backend

MarketLink is a farmer-to-customer marketplace that connects farmers directly with customers. The backend provides RESTful API endpoints for user authentication, account management, and other marketplace operations.

## Features

- **User Registration:** Register as a farmer or customer.
- **Email Verification:** Verify new accounts using an email verification code.
- **User Login:** Authenticate registered users and receive a JWT token.
- **Role-Based Access:** Support for customer and farmer accounts.
- **Password Recovery:** Request a password-reset code through email.
- **Password Reset:** Reset account passwords using a verification code.
- **Database Integration:** Store user and marketplace data in MySQL.
- **RESTful API:** Provide endpoints for communication with the MarketLink frontend.

## Technologies Used

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JSON Web Token (JWT)
- Maven
- Jakarta Mail for email functionality

## Project Structure

```text
MarketLink-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/BeninCodeX/MarketLink/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

*The structure above is illustrative. Your actual packages and folders may differ.*

## Getting Started

### Prerequisites

Install the following:

- Java Development Kit (JDK) compatible with the project's Java version
- Maven, or use the included Maven Wrapper
- MySQL Server or a hosted MySQL database
- Git

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR-USERNAME/MarketLink-Backend.git
   ```

2. Open the project directory:

   ```bash
   cd MarketLink-Backend
   ```

3. Configure the database connection and email settings using environment variables.

4. Build the project:

   ```bash
   ./mvnw clean package
   ```

   On Windows, use:

   ```bash
   mvnw.cmd clean package
   ```

5. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:

   ```bash
   mvnw.cmd spring-boot:run
   ```

The server uses the configured application port. Spring Boot commonly defaults to port `8080`.

## Environment Configuration

Configure sensitive values through environment variables rather than committing passwords or secret keys to GitHub.

Typical settings include:

| Setting | Purpose |
|---|---|
| `DB_URL` | MySQL JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key used to sign JWTs |
| `MAIL_USERNAME` | Email account used to send verification messages |
| `MAIL_PASSWORD` | Email app password or configured SMTP credential |

Update your Spring Boot configuration to read the corresponding environment variables. The exact variable names must match your application's configuration.

**Security note:** Never commit database credentials, Gmail app passwords, JWT secrets, or other private keys to the repository.

## API Endpoints

The following endpoints are part of the current authentication workflow. Confirm the exact request fields and response formats in your source code.

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/signup` | Register a new account |
| `POST` | `/api/verify-signup-code` | Verify the signup code |
| `POST` | `/api/login` | Authenticate a user |
| `POST` | `/api/forgot-password` | Request a password-reset code |
| `POST` | `/api/reset-password` | Reset a password |

The frontend communicates with these endpoints using HTTP requests and JSON data.

## Deployment

The backend can be deployed to a Java-compatible hosting platform and connected to a hosted MySQL database.

Before deployment:

1. Create and configure your hosted MySQL database.
2. Add the database credentials and other secrets to the hosting platform's environment variables.
3. Configure the application's production port and CORS allowed origins.
4. Configure the SMTP settings for email verification and password recovery.
5. Build and deploy the application.
6. Test the deployed API endpoints.
7. Update the frontend API base URL to use the deployed backend URL.

## Frontend Integration

MarketLink's frontend consumes the backend API for authentication and marketplace functionality.

After deployment, configure the frontend to use the public backend URL instead of a local development address such as `http://localhost:8080`.

## Contributing

Contributions, suggestions, and bug reports are welcome. Create a branch for your changes and submit a pull request for review.

## License

No license has been specified yet. Add a license file if you intend to define permissions for reuse and distribution.

---

**MarketLink** — Connecting farmers and customers through a digital marketplace.
