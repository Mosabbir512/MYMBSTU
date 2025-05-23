package com.sdlc.pro.mymbstu.service;

import com.sdlc.pro.mymbstu.repository.PredefinedStudentRepository;
import com.sdlc.pro.mymbstu.model.PredefinedStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public List<PredefinedStudent> readStudentsFromCSV() throws IOException {
        List<PredefinedStudent> students = new ArrayList<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("predefinedstudent.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

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
                students.add(student);
            }
        }
        return students;
    }

    public void saveAllStudents(List<PredefinedStudent> students) {
        predefinedStudentRepository.saveAll(students);
    }

}
