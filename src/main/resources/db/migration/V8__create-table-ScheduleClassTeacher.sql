CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule_class_teacher (
    idAvailableDay UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    dayOfWeek VARCHAR(9),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    lesson_id UUID NOT NULL,
    schedule_class_id UUID,
    teacher_id UUID NOT NULL,
    FOREIGN KEY (lesson_id) REFERENCES lesson(id),
    FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id),
    FOREIGN KEY (teacher_id) REFERENCES teacher(id)
);
