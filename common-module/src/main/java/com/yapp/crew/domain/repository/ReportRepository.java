package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

	List<Report> findAll();

	Report save(Report report);
}
