package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.HiddenBoard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiddenBoardRepository extends JpaRepository<HiddenBoard, Long> {

	List<HiddenBoard> findAllByUserId(Long userId);

	HiddenBoard save(HiddenBoard hiddenBoard);
}
