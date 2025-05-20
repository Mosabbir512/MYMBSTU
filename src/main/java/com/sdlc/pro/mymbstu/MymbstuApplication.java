package com.sdlc.pro.mymbstu;

import com.sdlc.pro.mymbstu.model.PredefinedStudent;
import com.sdlc.pro.mymbstu.service.DiaryImportService;
import com.sdlc.pro.mymbstu.service.PredefinedStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
public class
MymbstuApplication implements CommandLineRunner {
    @Autowired
    private DiaryImportService diaryImportService;

    @Autowired
    private PredefinedStudentService predefinedStudentService;


    public static void main(String[] args) {
        SpringApplication.run(MymbstuApplication.class, args);
    }

    @Override
    public void run(String... args) {
       // diaryImportService.importCsv();


        try {

            List<PredefinedStudent> students = predefinedStudentService.readStudentsFromCSV();
            //predefinedStudentService.saveAllStudents(students);
            System.out.println("Successfully loaded " + students.size() + " predefined students into database.");
        } catch (Exception e) {
            System.err.println("Failed to load predefined students: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
