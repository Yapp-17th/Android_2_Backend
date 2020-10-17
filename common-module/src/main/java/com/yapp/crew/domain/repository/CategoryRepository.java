package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findAll();

  Category save(Category category);
}
