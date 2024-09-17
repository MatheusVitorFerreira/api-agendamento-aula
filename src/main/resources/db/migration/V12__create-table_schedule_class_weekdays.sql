CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE schedule_class_weekdays (
    schedule_class_id UUID NOT NULL,
    week_day VARCHAR(255) NOT NULL,
    PRIMARY KEY (schedule_class_id, week_day),

    CONSTRAINT fk_schedule_class FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class_schedule)
);
