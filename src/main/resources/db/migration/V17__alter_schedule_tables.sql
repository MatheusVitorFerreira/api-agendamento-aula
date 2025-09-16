ALTER TABLE schedule_class
    ADD COLUMN IF NOT EXISTS date DATE NOT NULL;

ALTER TABLE schedule_class_student
DROP COLUMN IF EXISTS day_of_week;

ALTER TABLE schedule_class_teacher
DROP COLUMN IF EXISTS days_of_week;

DROP TABLE IF EXISTS schedule_class_weekdays;
DROP TABLE IF EXISTS schedule_class_teacher_days;
