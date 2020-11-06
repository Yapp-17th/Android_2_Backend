package com.yapp.crew.controller;

import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.utils.EnumToList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExerciseListController {

	@GetMapping(path = "/v1/exercise")
	public ResponseEntity<?> getExerciseList() {

		List<String> exerciseList = EnumToList.exerciseEnumToList();
		EnumListDto enumListDto = EnumListDto.pass(ResponseDomain.EXERCISE.getName(), exerciseList);

		return ResponseEntity.ok().body(enumListDto);
	}
}
