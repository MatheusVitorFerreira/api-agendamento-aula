CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE TimeTable (
idAvailableDay UUID PRIMARY KEY,
dayOfWeek VARCHAR(9),
startTime TIME,
endTime TIME,
teacher_id UUID,
FOREIGN KEY (teacher_id) REFERENCES Teacher(idTeacher) ON DELETE SET NULL
);
