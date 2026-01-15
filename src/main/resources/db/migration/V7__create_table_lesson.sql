CREATE TYPE status_class AS ENUM (
    'PENDING',
    'CONFIRMED',
    'CANCELLED'
);

CREATE TABLE lesson (
                        id_lesson UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

                        title VARCHAR(255) NOT NULL,
                        description TEXT,

                        teacher_id UUID NOT NULL,
                        classroom_id UUID,

                        status status_class DEFAULT 'CONFIRMED',

                        CONSTRAINT fk_lesson_teacher
                            FOREIGN KEY (teacher_id)
                                REFERENCES teacher(teacher_id)
                                ON DELETE CASCADE,

                        CONSTRAINT fk_lesson_classroom
                            FOREIGN KEY (classroom_id)
                                REFERENCES classroom(classroom_id)
                                ON DELETE SET NULL
);
