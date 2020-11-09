package com.yapp.crew.controller;

import com.yapp.crew.service.ApplyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplyListController {

	private ApplyListService applyListService;

	@Autowired
	public ApplyListController(ApplyListService applyListService) {
		this.applyListService = applyListService;
	}


}
