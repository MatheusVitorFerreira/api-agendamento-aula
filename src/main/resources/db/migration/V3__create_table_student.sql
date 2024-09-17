CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE student (
    student_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    enrollment_date DATE
);