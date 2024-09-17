CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule_class_student (
    student_scheduling_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    schedule_class_id UUID NOT NULL,
    student_id UUID NOT NULL,
    day_of_week VARCHAR(255) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    CONSTRAINT fk_schedule_class FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class_schedule),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(student_id)
);
