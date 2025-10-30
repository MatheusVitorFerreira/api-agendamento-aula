CREATE TABLE mural_comment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    post_id UUID NOT NULL,
    author_id UUID NOT NULL,
    CONSTRAINT fk_post
    FOREIGN KEY (post_id)
    REFERENCES mural_post(id)
    ON DELETE CASCADE,
    CONSTRAINT fk_author
    FOREIGN KEY (author_id)
    REFERENCES "users"(user_id)
    ON DELETE CASCADE
);
