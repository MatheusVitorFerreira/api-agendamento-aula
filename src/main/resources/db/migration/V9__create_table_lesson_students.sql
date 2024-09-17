CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE lesson_students (
    lesson_id UUID NOT NULL,
    student_id UUID NOT NULL,
    PRIMARY KEY (lesson_id, student_id),

    CONSTRAINT fk_lesson FOREIGN KEY (lesson_id) REFERENCES lesson(id_lesson),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(student_id)
);

