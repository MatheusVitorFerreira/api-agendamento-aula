CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE schedule_class_teacher_days (
                                             teacher_scheduling_id UUID NOT NULL,
                                             day_of_week VARCHAR(255) NOT NULL,
                                             PRIMARY KEY (teacher_scheduling_id, day_of_week),

                                             CONSTRAINT fk_schedule_class_teacher FOREIGN KEY (teacher_scheduling_id) REFERENCES schedule_class_teacher(teacher_scheduling_id)
);
