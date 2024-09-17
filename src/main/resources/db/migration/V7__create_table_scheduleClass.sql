CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule_class (
    id_class_schedule UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    class_shift VARCHAR(255),
    teacher_id UUID,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);

