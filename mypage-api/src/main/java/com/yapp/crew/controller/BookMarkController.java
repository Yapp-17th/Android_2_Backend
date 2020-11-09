package com.yapp.crew.controller;

import com.yapp.crew.service.BookMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookMarkController {

	private BookMarkService bookMarkService;

	@Autowired
	public BookMarkController(BookMarkService bookMarkService){
		this.bookMarkService = bookMarkService;
	}
}
