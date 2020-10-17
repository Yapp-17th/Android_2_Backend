package com.yapp.crew.domain.repository;

import java.util.List;

import com.yapp.crew.domain.model.Alarm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

  List<Alarm> findAll();

  Alarm save(Alarm alarm);
}
