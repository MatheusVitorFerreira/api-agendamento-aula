CREATE TABLE mural_post (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            title VARCHAR(255),
                            content TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            author_id UUID,
                            lesson_id UUID,
                            classroom_id UUID,

                            CONSTRAINT fk_muralpost_author
                                FOREIGN KEY (author_id)
                                    REFERENCES tb_users(user_id)
                                    ON DELETE SET NULL,

                            CONSTRAINT fk_muralpost_lesson
                                FOREIGN KEY (lesson_id)
                                    REFERENCES lesson(id_lesson)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_muralpost_classroom
                                FOREIGN KEY (classroom_id)
                                    REFERENCES classroom(classroom_id)
                                    ON DELETE CASCADE
);
