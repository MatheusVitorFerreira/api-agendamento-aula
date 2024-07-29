CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE enrollment (
id_enrollment UUID PRIMARY KEY DEFAULT gen_random_uuid(),
student_id UUID NOT NULL,
schedule_class_id UUID NOT NULL,
status VARCHAR(50) NOT NULL,
FOREIGN KEY (student_id) REFERENCES student(id_student),
FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class)
);
