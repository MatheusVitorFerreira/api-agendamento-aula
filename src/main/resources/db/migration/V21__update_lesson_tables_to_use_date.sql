DROP TABLE IF EXISTS lesson CASCADE;

CREATE TABLE lesson (
    id_lesson UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    teacher_id UUID NOT NULL,
    classroom_id UUID,
    status VARCHAR(50) DEFAULT 'CONFIRMED',
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id),
    CONSTRAINT fk_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id_class)
);
