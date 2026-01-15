CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE classroom (
                           classroom_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           name VARCHAR(255) NOT NULL,
                           description TEXT,

                           teacher_id UUID NOT NULL,

                           created_at TIMESTAMP DEFAULT NOW() NOT NULL,

                           CONSTRAINT fk_classroom_teacher
                               FOREIGN KEY (teacher_id)
                                   REFERENCES teacher(teacher_id)
                                   ON DELETE CASCADE
);
