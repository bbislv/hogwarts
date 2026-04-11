package com.example.hogwarts.repository;

import com.example.hogwarts.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Page<Avatar> findAll(Pageable pageable);
}