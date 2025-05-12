CREATE TABLE IF NOT EXISTS contacts (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    phone_number VARCHAR(255) NOT NULL,
    formatted_phone VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255),
    search_vector tsvector
);


CREATE INDEX IF NOT EXISTS idx_contacts_search_vector ON contacts USING GIN (search_vector);

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX IF NOT EXISTS trgm_idx_formatted_phone ON contacts USING GIN (formatted_phone gin_trgm_ops);

-- Create a function to generate the tsvector from multiple columns
CREATE OR REPLACE FUNCTION contacts_search_vector_update()
RETURNS trigger AS '
BEGIN
  NEW.search_vector =
    setweight(to_tsvector(''english'', COALESCE(NEW.first_name, '''')), ''A'') ||
    setweight(to_tsvector(''english'', COALESCE(NEW.last_name, '''')), ''B'') ||
    setweight(to_tsvector(''english'', COALESCE(NEW.address, '''')), ''C'');
  RETURN NEW;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER contacts_search_vector_update
    BEFORE INSERT OR UPDATE ON contacts
    FOR EACH ROW EXECUTE FUNCTION contacts_search_vector_update();
