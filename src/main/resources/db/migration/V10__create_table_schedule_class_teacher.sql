CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule_class_teacher (
    teacher_scheduling_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    days_of_week VARCHAR[] NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    lesson_id UUID NOT NULL,
    schedule_class_id UUID,
    teacher_id UUID NOT NULL,

    CONSTRAINT fk_lesson FOREIGN KEY (lesson_id) REFERENCES lesson(id_lesson),
    CONSTRAINT fk_schedule_class FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class_schedule),
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id)
);

