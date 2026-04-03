package com.example.hogwarts.service;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty save(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> findById(Long id) {
        return facultyRepository.findById(id);
    }

    public void deleteById(Long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty findByName(String name) {
        return facultyRepository.findByName(name);
    }

    public List<Faculty> searchByNameOrColor(String query) {
        List<Faculty> byName = facultyRepository.findByNameIgnoreCaseContaining(query);
        List<Faculty> byColor = facultyRepository.findByColorIgnoreCaseContaining(query);

        byName.addAll(byColor);
        return byName.stream().distinct().toList();
    }
}