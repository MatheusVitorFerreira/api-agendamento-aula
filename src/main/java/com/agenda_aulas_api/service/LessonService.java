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

    private static final String PARENT_TYPE = "LESSON";


    public List<Map<String, Object>> findAll() {
        try {
            return lessonRepository.findAll().stream()
                    .map(lesson -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("idLesson", lesson.getIdLesson());
                        map.put("classroom", lesson.getClassroom() != null ? lesson.getClassroom().getName() : "Sem turma");
                        map.put("scheduleId", lesson.getSchedule() != null ? lesson.getSchedule().getIdSchedule() : "Não agendada");
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
        lesson.setMuralPosts(muralPostRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE));

        return LessonRequestRecordDTO.fromLesson(lesson);
    }

    public Page<LessonRequestRecordDTO> findPageLesson(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        lessonPage.getContent().forEach(lesson -> {
            lesson.setMaterials(materialRepository.findByParentIdAndParentType(lesson.getIdLesson(), PARENT_TYPE));
            lesson.setMuralPosts(muralPostRepository.findByParentIdAndParentType(lesson.getIdLesson(), PARENT_TYPE));
        });

        return lessonPage.map(LessonRequestRecordDTO::fromLesson);
    }

    @Transactional
    public LessonRequestRecordDTO createLesson(LessonRequestRecordDTO dto) {
        Lesson lesson = dto.toLesson();

        Teacher teacher = teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher não encontrado com id: " + dto.teacherId()));
        lesson.setTeacher(teacher);

        if (dto.classroomId() != null) {
            Classroom classroom = classroomRepository.findById(dto.classroomId())
                    .orElseThrow(() -> new RuntimeException("Classroom não encontrada com id: " + dto.classroomId()));
            classroom.addLesson(lesson);
        }

        if (lesson.getSchedule() != null) {
            lesson.getSchedule().setLesson(lesson);
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        UUID lessonId = savedLesson.getIdLesson();


        if (dto.materials() != null && !dto.materials().isEmpty()) {
            var materials = dto.materials().stream()
                    .map(m -> {
                        Material mat = m.toEntity();
                        mat.setParentId(lessonId);
                        mat.setParentType(PARENT_TYPE);
                        return mat;
                    })
                    .collect(Collectors.toList());
            materialRepository.saveAll(materials);
            savedLesson.setMaterials(materials);
        }

        if (dto.muralPosts() != null && !dto.muralPosts().isEmpty()) {
            var posts = dto.muralPosts().stream()
                    .map(pdto -> {
                        User author = userRepository.findById(pdto.authorId())
                                .orElseThrow(() -> new RuntimeException("Autor do post não encontrado: " + pdto.authorId()));
                        MuralPost post = pdto.toEntity();
                        post.setParentId(lessonId);
                        post.setParentType(PARENT_TYPE);
                        post.setAuthor(author);
                        return post;
                    })
                    .collect(Collectors.toList());
            muralPostRepository.saveAll(posts);
            savedLesson.setMuralPosts(posts);
        }

        return LessonRequestRecordDTO.fromLesson(savedLesson);
    }

    @Transactional
    public LessonRequestRecordDTO updateLesson(LessonRequestRecordDTO dto, UUID idLesson) {
        Lesson lesson = lessonRepository.findById(idLesson)
                .orElseThrow(() -> new LessonNotFoundException("Lesson não encontrada com id: " + idLesson));

        if (dto.title() != null) lesson.setTitle(dto.title());
        if (dto.description() != null) lesson.setDescription(dto.description());
        if (dto.status() != null) lesson.setStatus(dto.status());

        if (dto.teacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.teacherId())
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher não encontrado com id: " + dto.teacherId()));
            lesson.setTeacher(teacher);
        }

        if (dto.classroomId() != null) {
            Classroom newClassroom = classroomRepository.findById(dto.classroomId())
                    .orElseThrow(() -> new RuntimeException("Classroom não encontrada com id: " + dto.classroomId()));
            Classroom current = lesson.getClassroom();
            if (current == null || !Objects.equals(current.getIdClass(), newClassroom.getIdClass())) {
                if (current != null) current.getLessons().remove(lesson);
                newClassroom.addLesson(lesson);
            }
        }


        if (lesson.getSchedule() != null
                && dto.date() != null
                && dto.startTime() != null
                && dto.endTime() != null
                && dto.shift() != null) {
            Schedule schedule = lesson.getSchedule();
            schedule.setDate(dto.date());
            schedule.setStartTime(dto.startTime());
            schedule.setEndTime(dto.endTime());
            schedule.setShift(dto.shift());
        }

        if (dto.materials() != null) {
            materialRepository.deleteAll(materialRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE));
            var newMaterials = dto.materials().stream()
                    .map(m -> {
                        Material mat = m.toEntity();
                        mat.setParentId(idLesson);
                        mat.setParentType(PARENT_TYPE);
                        return mat;
                    })
                    .collect(Collectors.toList());
            materialRepository.saveAll(newMaterials);
            lesson.setMaterials(newMaterials);
        }

        if (dto.muralPosts() != null) {
            var oldPosts = muralPostRepository.findByParentIdAndParentType(idLesson, PARENT_TYPE);
            if (!oldPosts.isEmpty()) {
                muralPostRepository.deleteAll(oldPosts);
            }

            var newPosts = dto.muralPosts().stream()
                    .map(pdto -> {
                        User author = userRepository.findById(pdto.authorId())
                                .orElseThrow(() -> new RuntimeException("Autor do post não encontrado: " + pdto.authorId()));
                        MuralPost post = pdto.toEntity();
                        post.setParentId(idLesson);
                        post.setParentType(PARENT_TYPE);
                        post.setAuthor(author);
                        return post;
                    })
                    .collect(Collectors.toList());
            muralPostRepository.saveAll(newPosts);
            lesson.setMuralPosts(newPosts);
        }

        Lesson updated = lessonRepository.save(lesson);
        return LessonRequestRecordDTO.fromLesson(updated);
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
