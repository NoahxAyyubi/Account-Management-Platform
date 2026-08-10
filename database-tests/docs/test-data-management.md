# Test Data Management

This repo keeps database validation tests separate from REST, Selenium, and Playwright automation so each framework stays easy to reason about.

## Local Setup

1. Create a disposable PostgreSQL database, for example `account_management_qa`.
2. Run `db/schema.sql`.
3. Run `db/seed-data.sql` if you want starter rows while learning.
4. Copy the values from the repo-level `.env.example` into your local environment or update `src/test/resources/config-qa.properties`.
5. Replace `change_me_locally` with your own local password. Do not commit real secrets.

## Naming Test Data

Use fake, test-owned values that are easy to clean up:

- Emails ending in `@db-test.local`
- Plan names like `BASIC`, `PREMIUM`, or `ENTERPRISE`
- Status values like `ACTIVE`, `CANCELLED`, or `PAST_DUE`

## Cleanup Rule

`TestDataCleanup` removes records matching `%@db-test.local`. Keep your practice data inside that pattern until you are comfortable writing safer cleanup rules.

## Learning Exercises

- Add one query to validate a cancelled subscription.
- Add one assertion that checks `created_at` is not null.
- Add one cleanup method for a second table after you create it.
- Connect this module to a REST or UI flow by creating data through the app first, then validating the saved row in PostgreSQL.

Run database tests from the module folder:

```bash
mvn test -Dgroups=database
```
