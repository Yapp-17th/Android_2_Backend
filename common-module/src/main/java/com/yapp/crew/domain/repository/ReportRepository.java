package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

  List<Report> findAll();

  List<Report> findAllByReportedId(Long userId);

  Report save(Report report);
}
