package com.yapp.crew.domain.repository;

import com.yapp.crew.domain.model.HiddenBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiddenBoardRepository extends JpaRepository<HiddenBoard, Long> {

	HiddenBoard save(HiddenBoard hiddenBoard);
}
