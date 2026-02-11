# Account-Management-Platform
Enterprise-level test automation framework demo for continuously validating critical features of the Account Management Portal, including authentication, billing, subscription management, and plan changes.

## Tech Stack
- Java
- Python
- Maven
- REST Assured (API testing)
- Selenium WebDriver (UI automation)
- JUnit / TestNG
- GitHub Actions / Jenkins (CI/CD)
- Docker (containerized test execution)

## Project Structure
account-management-platform/
```text
├── src/test/java/
│   ├── auth/              # Authentication & session tests
│   ├── subscription/      # Subscription management tests
│   ├── billing/           # Billing & invoice tests
│   └── plans/             # Plan change & entitlement tests
├── pom.xml
├── README.md
```
## Test Coverage
- Authentication and user session validation
- Subscription creation, update, and cancellation
- Billing and invoice visibility
- Plan upgrade and downgrade workflows

## Running Tests Locally
```bash
mvn test
```
Tests can also be executed via CI/CD pipelines on pull requests and merges.

## CI/CD
Tests are designed to run automatically as part of a CI/CD pipeline to ensure continuous validation of critical account management functionality.

## Notes
This framework is designed to be modular and extensible as new account management features are introduced.

