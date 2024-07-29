CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE teacher_discipline (
teacher_id UUID NOT NULL,
discipline_id UUID NOT NULL,

FOREIGN KEY (teacher_id) REFERENCES teacher(id_teacher),
FOREIGN KEY (discipline_id) REFERENCES discipline(id_discipline),

PRIMARY KEY (teacher_id, discipline_id)
);
