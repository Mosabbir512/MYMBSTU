package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.repository.PredefinedStudentRepository;
import com.sdlc.pro.mymbstu.model.PredefinedStudent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PredefinedStudentService {



    @Autowired
   private PredefinedStudentRepository predefinedStudentRepository;
    public Optional<PredefinedStudent> searchPredefinedStudent(String idPre,String namePre, String departmentPre, String sessionPre,String hallPre) {
       // System.out.println("from service ");
        System.out.println(idPre+" "+namePre+" "+departmentPre+" "+sessionPre+" "+hallPre);
        System.out.println(predefinedStudentRepository.findByIdPre(idPre));
        return predefinedStudentRepository.findByIdPreAndNamePreAndDepartmentPreAndSessionPreAndHallPre(idPre, namePre,departmentPre, sessionPre,hallPre);
    }
    public PredefinedStudent  findStudentById(String idPre) {
        Optional<PredefinedStudent> predefinedStudentOpt=predefinedStudentRepository.findByIdPre(idPre);

        return predefinedStudentOpt.orElse(null);
    }

    @Transactional
    public List<PredefinedStudent> readStudentsFromCSV() throws IOException {
        List<PredefinedStudent> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("predefinedstudent.csv").getFile()))) {
            br.readLine();

            // Skip header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                PredefinedStudent student = new PredefinedStudent(
                        data[0], // id
                        data[1], // name
                        data[2], // department
                        data[3], // session
                        data[4]  // hall
                );
                predefinedStudentRepository.save(student);
                System.out.println(student.toString());
                students.add(student);
            }
        }
        return students;
    }

      @Transactional
    public List<PredefinedStudent> getAllStudents() {
        predefinedStudentRepository.deleteAll();
        return predefinedStudentRepository.findAllByOrderByIdPreAsc();
    }



}
