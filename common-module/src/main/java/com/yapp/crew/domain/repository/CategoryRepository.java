package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAll();

	Category save(Category category);

	Optional<Category> findCategoryById(long id);
}
