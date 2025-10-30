CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE classroom (
    id_class UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    teacher_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);