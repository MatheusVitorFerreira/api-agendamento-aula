
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE discipline (
    id_discipline UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);
