CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule_class_students (
    id_class UUID NOT NULL,
    id_student UUID NOT NULL,

    FOREIGN KEY (id_class) REFERENCES schedule_class(id_class),
    FOREIGN KEY (id_student) REFERENCES student(id_student),

    PRIMARY KEY (id_class, id_student)
);
