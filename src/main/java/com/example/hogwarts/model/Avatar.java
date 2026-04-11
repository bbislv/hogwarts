package com.example.hogwarts.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    private Long fileSize;

    private String mediaType;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar(String filePath, Long fileSize, String mediaType) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
    }
}