package com.yapp.crew.controller;

import com.yapp.crew.model.UserAuthResponse;
import com.yapp.crew.service.WithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class WithdrawController {

  private WithdrawService withdrawService;

  @Autowired
  public WithdrawController(WithdrawService withdrawService) {
    this.withdrawService = withdrawService;
  }

  @DeleteMapping("/v1/user/withdraw")
  public ResponseEntity<?> deleteUser(@RequestHeader(value = "Authorization") String token) {

    UserAuthResponse userAuthResponse = withdrawService.withdraw(token);
    if (userAuthResponse.getUserAuthResponseBody().isSuccess()) {
      return ResponseEntity.ok(userAuthResponse.getUserAuthResponseBody());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userAuthResponse.getUserAuthResponseBody());
  }
}
