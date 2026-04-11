package com.example.hogwarts.service;

import com.example.hogwarts.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadService {

    public synchronized void printStudentName(String name, String threadName) {
        System.out.println(threadName + ": " + name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printStudentsSynchronized(List<Student> students) {
        for (int i = 0; i < Math.min(2, students.size()); i++) {
            printStudentName(students.get(i).getName(), "Main thread");
        }

        if (students.size() > 3) {
            new Thread(() -> {
                for (int i = 2; i < Math.min(4, students.size()); i++) {
                    printStudentName(students.get(i).getName(), "Thread 1");
                }
            }).start();
        }

        if (students.size() > 5) {
            new Thread(() -> {
                for (int i = 4; i < Math.min(6, students.size()); i++) {
                    printStudentName(students.get(i).getName(), "Thread 2");
                }
            }).start();
        }
    }
}