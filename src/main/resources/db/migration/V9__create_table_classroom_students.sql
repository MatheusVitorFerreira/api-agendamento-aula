CREATE TABLE classroom_students (
                                    classroom_id UUID NOT NULL,
                                    student_id UUID NOT NULL,

                                    PRIMARY KEY (classroom_id, student_id),

                                    CONSTRAINT fk_classroom_students_classroom
                                        FOREIGN KEY (classroom_id)
                                            REFERENCES classroom(classroom_id)
                                            ON DELETE CASCADE,

                                    CONSTRAINT fk_classroom_students_student
                                        FOREIGN KEY (student_id)
                                            REFERENCES student(student_id)
                                            ON DELETE CASCADE
);
