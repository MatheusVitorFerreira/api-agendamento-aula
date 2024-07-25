CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE teacher (
    id_teacher UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job VARCHAR(255),
    limit_courses_by_week INTEGER
);
