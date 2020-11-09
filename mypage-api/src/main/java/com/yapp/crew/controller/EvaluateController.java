package com.yapp.crew.controller;

import com.yapp.crew.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluateController {

	private EvaluateService evaluateService;

	@Autowired
	public EvaluateController(EvaluateService evaluateService){
		this.evaluateService = evaluateService;
	}
}
