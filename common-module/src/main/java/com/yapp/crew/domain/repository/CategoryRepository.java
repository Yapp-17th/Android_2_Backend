package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findAll();

  Category save(Category category);
}
