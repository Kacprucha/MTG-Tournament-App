# Keycloak 101

Quick instructions on what is what, where things are, and what they do.

## Key Points

As a good practice, security was set up so that **all endpoints require access from a verified user**. You cannot access any endpoint without a valid JWT token.
This makes it easier to protect certain endpoints, because we can simply check the user’s role (e.g., `ADMIN`) instead of writing separate security clauses for every endpoint.

Data from Keycloak is stored in a **MySQL database**, and we should **not send requests directly to it**. All user information should be accessed via the JWT token.

All configuration for Keycloak (realms, roles, clients) is stored in the file **`app-realm.json`** inside this folder.
This file is loaded when the Keycloak Docker image is built, so every app build will have the same security settings.

To access the **Admin Console**, go to:

```
http://localhost:8443/admin/master/console/#/
```

From there, you can create test users.

⚠ **Important:** Tokens cannot be generated if the user profile is incomplete. In addition to `username` and `password`, the user must have an **email**, **first name**, and **last name**. This should be fixed in the near future.

## Generating a JWT Token

You can generate a JWT token using **Postman** or **PowerShell**.
Example PowerShell command:

```bash
curl.exe --location --request POST 'http://localhost:8443/realms/app/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=frontend-web' \
--data-urlencode 'username=USERNAME_OF_THE_USER' \
--data-urlencode 'password=USER_PASSWORD' \
--data-urlencode 'grant_type=password'
```

## Using OpenAPI

As mentioned earlier, **all endpoints require a valid JWT token**.
When testing with OpenAPI:

1. Generate a JWT token as described above.
2. In the OpenAPI UI, click **Authorize** on the right side.
3. In the popup, paste your JWT token.

## Important Files

In the `SecurityConfig` class, you can specify paths that **do not require** a valid JWT token (this is why the OpenAPI UI is accessible).
More exceptions can be added if necessary, but it’s not recommended.

---

## Known Issues

1. **User Data Requirement** – Tokens cannot be generated unless the user has email, first name, and last name in addition to username and password.
2. **Hardcoded URIs** – In `application.yml`, the fields:

   ```
   spring.security.oauth2.resource-server.jwt.issuer-uri
   spring.security.oauth2.resource-server.jwt.jwk-set-uri
   ```

   are currently hardcoded. An attempt to make them dynamic caused the application to reject valid tokens.
   Not urgent, but worth fixing for better maintainability.
