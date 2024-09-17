CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE teacher (
    teacher_id UUID PRIMARY KEY DEFAULT uuid_generate_v4()
);
