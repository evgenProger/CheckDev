ALTER TABLE IF EXISTS interview
    ADD COLUMN IF NOT EXISTS count_wishers int;

UPDATE interview
SET count_wishers = '0'
WHERE author IS NULL;