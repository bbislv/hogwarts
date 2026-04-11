package com.example.hogwarts.service;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty save(Faculty faculty) {
        logger.info("Was invoked method for create/update faculty");
        logger.debug("Saving faculty with name: {}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAll() {
        logger.info("Was invoked method for find all faculties");
        logger.debug("Finding all faculties");
        return facultyRepository.findAll();
    }

    public Optional<Faculty> findById(Long id) {
        logger.info("Was invoked method for find faculty by id: {}", id);
        logger.debug("Searching faculty with id: {}", id);
        return facultyRepository.findById(id);
    }

    public void deleteById(Long id) {
        logger.info("Was invoked method for delete faculty by id: {}", id);
        if (facultyRepository.existsById(id)) {
            logger.debug("Deleting faculty with id: {}", id);
            facultyRepository.deleteById(id);
        } else {
            logger.warn("Faculty with id {} not found for deletion", id);
        }
    }

    public Faculty findByName(String name) {
        logger.info("Was invoked method for find faculty by name: {}", name);
        logger.debug("Searching faculty with name: {}", name);
        return facultyRepository.findByName(name);
    }

    public List<Faculty> searchByNameOrColor(String query) {
        logger.info("Was invoked method for search faculty by query: {}", query);
        logger.debug("Searching faculty with query: {}", query);
        List<Faculty> byName = facultyRepository.findByNameIgnoreCaseContaining(query);
        List<Faculty> byColor = facultyRepository.findByColorIgnoreCaseContaining(query);

        byName.addAll(byColor);
        return byName.stream().distinct().toList();
    }
}