package com.agenda_aulas_api.service;

import com.agenda_aulas_api.domain.*;
import com.agenda_aulas_api.dto.record.LessonRequestRecordDTO;
import com.agenda_aulas_api.exception.erros.DatabaseNegatedAccessException;
import com.agenda_aulas_api.exception.erros.LessonNotFoundException;
import com.agenda_aulas_api.exception.erros.TeacherNotFoundException;
import com.agenda_aulas_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;
    private final MaterialRepository materialRepository;
    private final MuralPostRepository muralPostRepository;
    private final UserRepository userRepository;

    private final String PARENT_TYPE = "LESSON";

    public List<Map<String, Object>> findAll() {
        try {
            return lessonRepository.findAll().stream()
                    .map(lesson -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("idLesson", lesson.getIdLesson());
                        map.put("classroom", lesson.getClassroom() != null ? lesson.getClassroom().getName() : "Sem turma");
                        map.put("scheduleId", lesson.getSchedule() != null ? lesson.getSchedule().getIdSchedule() : "Não Agendada");
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseNegatedAccessException("Falha ao acessar o banco de dados: " + e.getMessage());
        }
    }

    public LessonRequestRecordDTO findById(UUID idLesson) {
        Lesson lesson = lessonRepository.findById(idLesson)
                .orElseThrow(() -> new LessonNotFoundException("Lesson não encontrada com id: " + idLesson));

        lesson.setMaterials(materialRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE));
        lesson.setPosts(muralPostRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE));

        return LessonRequestRecordDTO.fromLesson(lesson);
    }

    public Page<LessonRequestRecordDTO> findPageLesson(
            Integer page,
            Integer linesPerPage,
            String orderBy,
            String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        lessonPage.getContent().forEach(lesson -> {
            lesson.setMaterials(materialRepository.findByParentIdAndParentType(lesson.getIdLesson(), PARENT_TYPE));
            lesson.setPosts(muralPostRepository.findByParentIdAndParentType(lesson.getIdLesson(), PARENT_TYPE));
        });

        return lessonPage.map(LessonRequestRecordDTO::fromLesson);
    }

    @Transactional
    public LessonRequestRecordDTO createLesson(LessonRequestRecordDTO lessonRequestDTO) {
        Lesson lesson = lessonRequestDTO.toLesson();

        Teacher teacher = teacherRepository.findById(lessonRequestDTO.teacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher não encontrado com id: " + lessonRequestDTO.teacherId()));
        lesson.setTeacher(teacher);

        if (lessonRequestDTO.classroomId() != null) {
            Classroom classroom = classroomRepository.findById(lessonRequestDTO.classroomId())
                    .orElseThrow(() -> new RuntimeException("Classroom não encontrada com id: " + lessonRequestDTO.classroomId()));
            classroom.addLesson(lesson);
        }

        if (lesson.getSchedule() != null) {
            lesson.getSchedule().setLesson(lesson);
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        UUID lessonId = savedLesson.getIdLesson();

        if (lessonRequestDTO.materials() != null && !lessonRequestDTO.materials().isEmpty()) {
            List<Material> materials = lessonRequestDTO.materials().stream()
                    .map(dto -> {
                        Material m = dto.toEntity();
                        m.setParentId(lessonId);
                        m.setParentType(PARENT_TYPE);
                        return m;
                    })
                    .collect(Collectors.toList());
            materialRepository.saveAll(materials);
            savedLesson.setMaterials(materials);
        }

        if (lessonRequestDTO.posts() != null && !lessonRequestDTO.posts().isEmpty()) {
            List<MuralPost> posts = lessonRequestDTO.posts().stream()
                    .map(dto -> {
                        User author = userRepository.findById(dto.authorId())
                                .orElseThrow(() -> new RuntimeException("Autor do post não encontrado"));

                        MuralPost p = dto.toEntity();
                        p.setParentId(lessonId);
                        p.setParentType(PARENT_TYPE);
                        p.setAuthor(author);
                        return p;
                    })
                    .collect(Collectors.toList());
            muralPostRepository.saveAll(posts);
            savedLesson.setPosts(posts);
        }

        return LessonRequestRecordDTO.fromLesson(savedLesson);
    }

    @Transactional
    public LessonRequestRecordDTO updateLesson(LessonRequestRecordDTO lessonRequestDTO, UUID idLesson) {
        Lesson lesson = lessonRepository.findById(idLesson)
                .orElseThrow(() -> new LessonNotFoundException("Lesson não encontrada com id: " + idLesson));

        if (lessonRequestDTO.title() != null) {
            lesson.setTitle(lessonRequestDTO.title());
        }
        if (lessonRequestDTO.description() != null) {
            lesson.setDescription(lessonRequestDTO.description());
        }
        if (lessonRequestDTO.status() != null) {
            lesson.setStatus(lessonRequestDTO.status());
        }

        if (lessonRequestDTO.teacherId() != null) {
            Teacher teacher = teacherRepository.findById(lessonRequestDTO.teacherId())
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher não encontrado com id: " + lessonRequestDTO.teacherId()));
            lesson.setTeacher(teacher);
        }

        if (lessonRequestDTO.classroomId() != null) {
            Classroom existingClassroom = lesson.getClassroom();
            if (!Objects.equals(existingClassroom != null ? existingClassroom.getIdClass() : null, lessonRequestDTO.classroomId())) {
                if (existingClassroom != null) {
                    existingClassroom.getLessons().remove(lesson);
                }
                Classroom newClassroom = classroomRepository.findById(lessonRequestDTO.classroomId())
                        .orElseThrow(() -> new RuntimeException("Classroom não encontrada com id: " + lessonRequestDTO.classroomId()));
                newClassroom.addLesson(lesson);
            }
        }

        if (lesson.getSchedule() != null
                && lessonRequestDTO.date() != null
                && lessonRequestDTO.startTime() != null
                && lessonRequestDTO.endTime() != null
                && lessonRequestDTO.shift() != null) {
            Schedule schedule = lesson.getSchedule();
            schedule.setDate(lessonRequestDTO.date());
            schedule.setStartTime(lessonRequestDTO.startTime());
            schedule.setEndTime(lessonRequestDTO.endTime());
            schedule.setShift(lessonRequestDTO.shift());
        }

        if (lessonRequestDTO.materials() != null) {
            materialRepository.deleteAll(materialRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE));
            List<Material> newMaterials = lessonRequestDTO.materials().stream()
                    .map(dto -> {
                        Material m = dto.toEntity();
                        m.setParentId(idLesson);
                        m.setParentType(PARENT_TYPE);
                        return m;
                    })
                    .collect(Collectors.toList());
            materialRepository.saveAll(newMaterials);
            lesson.setMaterials(newMaterials);
        }

        if (lessonRequestDTO.posts() != null) {
            muralPostRepository.deleteAll(muralPostRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE));
            List<MuralPost> newPosts = lessonRequestDTO.posts().stream()
                    .map(dto -> {
                        User author = userRepository.findById(dto.authorId())
                                .orElseThrow(() -> new RuntimeException("Autor do post não encontrado"));
                        MuralPost p = dto.toEntity();
                        p.setParentId(idLesson);
                        p.setParentType(PARENT_TYPE);
                        p.setAuthor(author);
                        return p;
                    })
                    .collect(Collectors.toList());
            muralPostRepository.saveAll(newPosts);
            lesson.setPosts(newPosts);
        }

        Lesson updatedLesson = lessonRepository.save(lesson);
        return LessonRequestRecordDTO.fromLesson(updatedLesson);
    }


    @Transactional
    public void deleteLesson(UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson não encontrada com id: " + lessonId));

        materialRepository.deleteAll(materialRepository.findByParentIdAndParentType(lessonId, PARENT_TYPE));
        muralPostRepository.deleteAll(muralPostRepository.findByParentIdAndParentType(lessonId, PARENT_TYPE));

        lessonRepository.delete(lesson);
    }
}