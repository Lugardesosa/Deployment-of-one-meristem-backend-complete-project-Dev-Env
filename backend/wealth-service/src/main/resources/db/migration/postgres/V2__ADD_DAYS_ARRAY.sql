ALTER TABLE investment_plan_settings ADD COLUMN days_array VARCHAR(200);

UPDATE investment_plan_settings
SET days_array = '30,60,90,180,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'fixed-term-investment-portfolio');

UPDATE investment_plan_settings
SET days_array = '30,60,90,180,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'meristem-money-market-fund');

UPDATE investment_plan_settings
SET days_array = '90,180,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'meristem-fixed-income-fund');

UPDATE investment_plan_settings
SET days_array = '30,60,90,180'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'commercial-paper');

UPDATE investment_plan_settings
SET days_array = '30,60,90,180,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'meristem-equity-market-fund');

UPDATE investment_plan_settings
SET days_array = '91,182,273,364'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'treasury-linked-investment');

UPDATE investment_plan_settings
SET days_array = '182,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'meristem-ethical-earnings-portfolio');

UPDATE investment_plan_settings
SET days_array = '180,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'meristem-dollar-fund');

UPDATE investment_plan_settings
SET days_array = '90,180,365'
WHERE investment_plan_id = (SELECT id FROM investment_plans WHERE slug = 'meristem-dollar-investment-portfolio');