SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id
ORDER BY s.name;

SELECT
    s.name AS student_name,
    s.age AS student_age,
    a.file_path AS avatar_path,
    a.media_type AS avatar_type
FROM student s
INNER JOIN avatar a ON s.id = a.student_id
ORDER BY s.name;