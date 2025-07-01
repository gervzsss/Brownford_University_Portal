package com.brownford.data;

import com.brownford.model.Program;
import com.brownford.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@org.springframework.context.annotation.DependsOn({"courseDataInitializer"})
public class ProgramDataInitializer implements CommandLineRunner {
    @Autowired
    private ProgramRepository programRepository;

    @Override
    public void run(String... args) {
        if (programRepository.count() == 0) {
            List<Program> programs = new ArrayList<>();

            Program p1 = new Program();
            p1.setCode("BSSTAT");
            p1.setName("Bachelor of Science in Statistics");
            p1.setStatus("Active");
            p1.setTotalUnits(62);
            p1.setYears(4);
            programs.add(p1);

            Program p2 = new Program();
            p2.setCode("BSA");
            p2.setName("Bachelor of Science in Accountancy");
            p2.setStatus("Active");
            p2.setTotalUnits(101);
            p2.setYears(4);
            programs.add(p2);

            Program p3 = new Program();
            p3.setCode("BSIT");
            p3.setName("Bachelor of Science in Information Technology");
            p3.setStatus("Active");
            p3.setTotalUnits(56);
            p3.setYears(4);
            programs.add(p3);

            Program p4 = new Program();
            p4.setCode("BSPSYCH");
            p4.setName("Bachelor of Science in Psychology");
            p4.setStatus("Active");
            p4.setTotalUnits(51);
            p4.setYears(4);
            programs.add(p4);

            Program p5 = new Program();
            p5.setCode("BSMM");
            p5.setName("Bachelor of Science in Marketing Management");
            p5.setStatus("Active");
            p5.setTotalUnits(62);
            p5.setYears(4);
            programs.add(p5);

            programRepository.saveAll(programs);

            System.out.println("[INFO] Default programs inserted.");

        }
    }
}
