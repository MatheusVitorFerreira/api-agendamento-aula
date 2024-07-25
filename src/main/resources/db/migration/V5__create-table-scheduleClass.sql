CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule_class (
    id_class UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date TIMESTAMP NOT NULL,
    start_hour TIME NOT NULL,
    end_hour TIME NOT NULL,
    id_teacher UUID NOT NULL,
    id_discipline UUID NOT NULL,
    location VARCHAR(255),
    status VARCHAR(50) NOT NULL,

    FOREIGN KEY (id_teacher) REFERENCES teacher(id_teacher),
    FOREIGN KEY (id_discipline) REFERENCES discipline(id_discipline)
);
