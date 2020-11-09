package com.yapp.crew.controller;

import com.yapp.crew.service.CommonProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonProfileController {

	private CommonProfileService commonProfileService;

	@Autowired
	public CommonProfileController(CommonProfileService commonProfileService){
		this.commonProfileService = commonProfileService;
	}
}
