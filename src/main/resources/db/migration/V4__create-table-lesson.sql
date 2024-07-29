CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE lesson (
id_lesson UUID PRIMARY KEY DEFAULT gen_random_uuid(),
student_id UUID,
teacher_id UUID,
schedule_class_id UUID,
week_day VARCHAR(9) NOT NULL,
start_time TIME NOT NULL,
end_time TIME NOT NULL,

FOREIGN KEY (student_id) REFERENCES student(id_student),
FOREIGN KEY (teacher_id) REFERENCES teacher(id_teacher),
FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class)
);
