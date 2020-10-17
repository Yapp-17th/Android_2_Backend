package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

  List<Alarm> findAll();

  Alarm save(Alarm alarm);
}
