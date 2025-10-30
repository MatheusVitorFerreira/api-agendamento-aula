CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_material (
        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        lesson_id UUID NOT NULL,
        file_name VARCHAR(255) NOT NULL,
        file_type VARCHAR(100) NOT NULL,
        file_url TEXT NOT NULL,
        upload_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_material_lesson FOREIGN KEY (lesson_id) REFERENCES lesson(id_lesson) ON DELETE CASCADE
);
