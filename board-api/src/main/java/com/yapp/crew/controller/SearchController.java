package com.yapp.crew.controller;

import com.yapp.crew.domain.auth.Auth;
import com.yapp.crew.dto.BoardFilterRequestDto;
import com.yapp.crew.dto.BoardListResponseDto;
import com.yapp.crew.dto.BoardSearchDto;
import com.yapp.crew.model.BoardFilter;
import com.yapp.crew.model.BoardResponseInfo;
import com.yapp.crew.model.BoardSearch;
import com.yapp.crew.service.SearchService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class SearchController {

  private SearchService searchService;
  private Auth auth;

  @Autowired
  public SearchController(SearchService searchService, Auth auth) {
    this.searchService = searchService;
    this.auth = auth;
  }

  @GetMapping(path = "/v1/board/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getBoardList(@RequestHeader(value = "Authorization") String token, @PageableDefault(size = 20, page = 0) Pageable pageable, @RequestBody @Valid BoardSearchDto boardSearchDto) {

    List<BoardResponseInfo> boardResponseInfoList = searchService.searchBoardList(BoardSearch.build(boardSearchDto));

    PagedListHolder<BoardResponseInfo> page = new PagedListHolder<>(boardResponseInfoList);
    page.setPageSize(pageable.getPageSize());
    page.setPage(pageable.getPageNumber());

    BoardListResponseDto boardListResponseDto = BoardListResponseDto.build(page.getPageList());
    return ResponseEntity.ok().body(boardListResponseDto);
  }
}
