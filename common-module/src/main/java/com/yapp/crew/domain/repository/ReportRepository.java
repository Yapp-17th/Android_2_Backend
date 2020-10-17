package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

  List<Report> findAll();

  List<Report> findAllByReportedId(Long userId);

  Report save(Report report);
}
