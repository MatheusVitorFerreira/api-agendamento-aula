
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE student (
id_student UUID PRIMARY KEY DEFAULT gen_random_uuid(),
enrollment_date DATE NOT NULL,
progress VARCHAR(255)
);
