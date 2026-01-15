CREATE TYPE class_shift AS ENUM (
    'MANHA',
    'TARDE',
    'NOITE'
);

CREATE TABLE schedule (
                          id_schedule UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

                          date DATE NOT NULL,
                          start_time TIME NOT NULL,
                          end_time TIME NOT NULL,

                          shift class_shift,

                          lesson_id UUID NOT NULL UNIQUE,

                          CONSTRAINT fk_schedule_lesson
                              FOREIGN KEY (lesson_id)
                                  REFERENCES lesson(id_lesson)
                                  ON DELETE CASCADE
);
