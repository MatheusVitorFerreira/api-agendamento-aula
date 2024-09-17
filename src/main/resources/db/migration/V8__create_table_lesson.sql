CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE lesson (
    id_lesson UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    teacher_id UUID NOT NULL,
    discipline_id UUID NOT NULL,
    available_slots INT,
    status VARCHAR(255),
    location VARCHAR(255),
    class_shift VARCHAR(255),
    schedule_class_id UUID,

    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id),
    CONSTRAINT fk_discipline FOREIGN KEY (discipline_id) REFERENCES discipline(id_discipline),
    CONSTRAINT fk_schedule_class FOREIGN KEY (schedule_class_id) REFERENCES schedule_class(id_class_schedule)
);

