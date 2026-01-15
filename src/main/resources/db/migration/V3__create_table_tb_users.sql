CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tb_users (
                          user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

                          username VARCHAR(255) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,

                          user_type VARCHAR(50) NOT NULL,

                          created_at TIMESTAMP DEFAULT NOW() NOT NULL
);
