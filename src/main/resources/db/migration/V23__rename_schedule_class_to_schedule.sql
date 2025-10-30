CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


ALTER TABLE IF EXISTS schedule_class RENAME TO schedule;


ALTER TABLE schedule
    RENAME COLUMN id_class_schedule TO id_schedule;


ALTER TABLE schedule
DROP CONSTRAINT IF EXISTS fk_teacher,
DROP COLUMN IF EXISTS teacher_id;


ALTER TABLE schedule
    RENAME COLUMN class_shift TO shift;

ALTER TABLE schedule
    ALTER COLUMN date SET NOT NULL,
ALTER COLUMN start_time SET NOT NULL,
ALTER COLUMN end_time SET NOT NULL;


ALTER TABLE schedule
    ADD COLUMN IF NOT EXISTS lesson_id UUID NOT NULL;


ALTER TABLE schedule
    ADD CONSTRAINT fk_lesson FOREIGN KEY (lesson_id) REFERENCES lesson(id_lesson);

ALTER TABLE schedule
    ALTER COLUMN id_schedule SET DEFAULT uuid_generate_v4();
