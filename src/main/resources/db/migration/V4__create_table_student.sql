CREATE TABLE student (
                         student_id UUID PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL,
                         age INT NOT NULL,
                         birth_date DATE,
                         address_id UUID,
                         cpf VARCHAR(14) NOT NULL UNIQUE,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         telephone VARCHAR(50),
                         enrollment_date DATE,
                         user_id UUID NOT NULL UNIQUE,

                         CONSTRAINT fk_student_user
                             FOREIGN KEY (user_id)
                                 REFERENCES tb_users(user_id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_student_address
                             FOREIGN KEY (address_id)
                                 REFERENCES address(address_id)
                                 ON DELETE SET NULL
);
