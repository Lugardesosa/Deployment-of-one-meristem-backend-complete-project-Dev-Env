ALTER TABLE investment_plans ADD COLUMN is_unique BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE investment_plans SET is_unique = FALSE WHERE slug = 'fixed-term-investment-portfolio';
UPDATE investment_plans SET is_unique = TRUE WHERE slug = 'meristem-money-market-fund';
UPDATE investment_plans SET is_unique = TRUE WHERE slug = 'meristem-fixed-income-fund';
UPDATE investment_plans SET is_unique = FALSE WHERE slug = 'commercial-paper';
UPDATE investment_plans SET is_unique = TRUE WHERE slug = 'meristem-equity-market-fund';
UPDATE investment_plans SET is_unique = FALSE WHERE slug = 'treasury-linked-investment';
UPDATE investment_plans SET is_unique = FALSE WHERE slug = 'meristem-ethical-earnings-portfolio';
UPDATE investment_plans SET is_unique = FALSE WHERE slug = 'meristem-dollar-fund';
UPDATE investment_plans SET is_unique = FALSE WHERE slug = 'meristem-dollar-investment-portfolio';