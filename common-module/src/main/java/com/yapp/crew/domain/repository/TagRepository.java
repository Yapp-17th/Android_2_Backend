package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findAll();

  Tag save(Tag tag);
}
