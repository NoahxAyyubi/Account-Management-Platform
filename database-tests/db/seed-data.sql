-- Starter data for local database validation exercises.
-- Keep seed records clearly fake and test-owned.

INSERT INTO plans (code, name, monthly_amount, status)
VALUES
    ('BASIC', 'Basic Plan', 29.99, 'ACTIVE'),
    ('PREMIUM', 'Premium Plan', 59.99, 'ACTIVE'),
    ('FAMILY', 'Family Plan', 89.99, 'ACTIVE')
ON CONFLICT (code)
DO UPDATE SET
    name = EXCLUDED.name,
    monthly_amount = EXCLUDED.monthly_amount,
    status = EXCLUDED.status;

INSERT INTO users (email, status)
VALUES
    ('happy_path_user@test.com', 'ACTIVE'),
    ('suspended_user@test.com', 'SUSPENDED')
ON CONFLICT (email)
DO UPDATE SET
    status = EXCLUDED.status;
