CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE teacher_disciplines (
    teacher_id UUID NOT NULL,
    discipline_id UUID NOT NULL,
    PRIMARY KEY (teacher_id, discipline_id),

    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id),
    CONSTRAINT fk_discipline FOREIGN KEY (discipline_id) REFERENCES discipline(id_discipline)
);
