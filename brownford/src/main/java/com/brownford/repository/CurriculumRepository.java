package com.brownford.repository;

import com.brownford.model.Curriculum;
import com.brownford.model.Program;
import com.brownford.model.Curriculum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
    List<Curriculum> findByProgram(Program program);

    List<Curriculum> findByProgramId(Long programId);

    List<Curriculum> findByProgramIdAndStatus(Long programId, Status status);

    Curriculum findByProgramIdAndYearEffective(Long programId, int yearEffective);
}
