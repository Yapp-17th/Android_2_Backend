package com.yapp.crew.domain.repository;

import java.util.List;

import com.yapp.crew.domain.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findAll();

  Category save(Category category);
}
