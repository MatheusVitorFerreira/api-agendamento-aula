CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE enrollment (
    id_enrollment UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL,
    schedule_class_id UUID NOT NULL,
    status VARCHAR(255) NOT NULL,
    enrollment_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(student_id),
    CONSTRAINT fk_schedule_class FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class_schedule)
);


