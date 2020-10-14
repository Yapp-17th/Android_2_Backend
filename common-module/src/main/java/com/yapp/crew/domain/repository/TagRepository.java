package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findAll();

  Tag save(Tag tag);
}
