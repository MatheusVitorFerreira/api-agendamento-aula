package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.Classroom;
import com.agenda_aulas_api.domain.MuralPost;
import com.agenda_aulas_api.domain.Student;
import com.agenda_aulas_api.domain.Teacher;
import com.agenda_aulas_api.dto.ClassroomDTO;
import com.agenda_aulas_api.dto.record.ManageStudentClassroomDTO;
import com.agenda_aulas_api.dto.record.ClassroomRequestRecordDTO;
import com.agenda_aulas_api.dto.record.ClassroomResponseRecordDTO;
import com.agenda_aulas_api.exception.erros.*;
import com.agenda_aulas_api.repository.ClassroomRepository;
import com.agenda_aulas_api.repository.StudentRepository;
import com.agenda_aulas_api.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    public ClassroomDTO findById(UUID id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException("Classroom não encontrada com id: " + id));
        return ClassroomDTO.fromLesson(classroom);
    }

    public List<ClassroomResponseRecordDTO> findAll() {
        return classroomRepository.findAll()
                .stream()
                .map(ClassroomResponseRecordDTO::fromClassRoom)
                .toList();
    }

    public Page<ClassroomRequestRecordDTO> findPage(
            Integer page,
            Integer linesPerPage,
            String orderBy,
            String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return classroomRepository.findAll(pageRequest)
                .map(ClassroomRequestRecordDTO::fromClassRoom);
    }

    @Transactional
    public ClassroomRequestRecordDTO createClassroom(ClassroomRequestRecordDTO dto) {
        Classroom classroom = dto.toClassRoom();
        Teacher teacher = teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Professor não encontrado com id: " + dto.teacherId()));
        classroom.setTeacher(teacher);

        if (dto.studentIds() != null && !dto.studentIds().isEmpty()) {
            Set<Student> students = new HashSet<>(studentRepository.findAllById(dto.studentIds()));

            if (students.size() != dto.studentIds().size()) {
                throw new StudentNotFoundException("Um ou mais alunos não foram encontrados.");
            }
            for (Student student : students) {
                classroom.addStudent(student);
            }
        }

        Classroom savedClassroom = classroomRepository.save(classroom);
        return ClassroomRequestRecordDTO.fromClassRoom(savedClassroom);
    }

    @Transactional
    public ClassroomRequestRecordDTO updateClassroom(UUID id, ClassroomRequestRecordDTO dto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException("Classroom não encontrada com id: " + id));

        classroom.setName(dto.name());
        classroom.setDescription(dto.description());

        if (dto.muralPosts() != null) {
            List<MuralPost> posts = dto.muralPosts().stream()
                    .map(postDTO -> {
                        MuralPost post = postDTO.toEntity();
                        post.setParentId(classroom.getIdClass());
                        post.setParentType("CLASSROOM");
                        return post;
                    })
                    .toList();

            classroom.setMuralPosts(posts);
        }

        updateStudentEnrollments(classroom, dto.studentIds());

        Classroom saved = classroomRepository.save(classroom);
        return ClassroomRequestRecordDTO.fromClassRoom(saved);
    }

    @Transactional
    public void deleteClassroom(UUID id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ClassroomNotFoundException("Classroom não encontrada com id: " + id));
        new HashSet<>(classroom.getStudents()).forEach(classroom::removeStudent);

        classroomRepository.delete(classroom);
    }

    private void updateStudentEnrollments(Classroom classroom, Set<UUID> newStudentIds) {
        Set<UUID> currentStudentIds = classroom.getStudents().stream()
                .map(Student::getStudentId)
                .collect(Collectors.toSet());

        if (newStudentIds == null) {
            newStudentIds = new HashSet<>();
        }

        Set<UUID> idsToRemove = new HashSet<>(currentStudentIds);
        idsToRemove.removeAll(newStudentIds);

        Set<Student> studentsToRemove = classroom.getStudents().stream()
                .filter(student -> idsToRemove.contains(student.getStudentId()))
                .collect(Collectors.toSet());

        studentsToRemove.forEach(classroom::removeStudent);


        Set<UUID> idsToAdd = new HashSet<>(newStudentIds);
        idsToAdd.removeAll(currentStudentIds);

        if (!idsToAdd.isEmpty()) {
            Set<Student> studentsToAdd = new HashSet<>(studentRepository.findAllById(idsToAdd));
            if(studentsToAdd.size() != idsToAdd.size()){
                throw new StudentNotFoundException("Um ou mais alunos para adicionar não foram encontrados.");
            }

            studentsToAdd.forEach(classroom::addStudent);
        }
    }

    @Transactional
    public ClassroomRequestRecordDTO addStudentToClassroom(ManageStudentClassroomDTO dto) {
        Classroom classroom = classroomRepository.findById(dto.classroomId())
                .orElseThrow(() -> new ClassroomNotFoundException(
                        "Classroom não encontrada com id: " + dto.classroomId()));

        Student student = studentRepository.findById(dto.studentId())
                .orElseThrow(() -> new StudentNotFoundException(
                        "Aluno não encontrado com id: " + dto.studentId()));

        if (classroom.getStudents().contains(student)) {
            throw new MatriculaDuplicadaException("O aluno já está matriculado nesta turma.");
        }

        classroom.addStudent(student);
        Classroom saved = classroomRepository.save(classroom);

        return ClassroomRequestRecordDTO.fromClassRoom(saved);
    }

    @Transactional
    public ClassroomRequestRecordDTO removeStudentFromClassroom(UUID classroomId, UUID studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ClassroomNotFoundException("Classroom não encontrada com id: " + classroomId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado com id: " + studentId));

        if (!classroom.getStudents().contains(student)) {
            throw new StudentNotFoundException("O aluno não está matriculado nesta turma.");
        }

        classroom.removeStudent(student);

        Classroom updated = classroomRepository.save(classroom);
        return ClassroomRequestRecordDTO.fromClassRoom(updated);
    }

}