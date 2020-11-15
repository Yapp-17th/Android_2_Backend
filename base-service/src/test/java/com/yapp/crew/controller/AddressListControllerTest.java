package com.yapp.crew.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.utils.EnumToList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EnumToList.class, AddressListController.class})
public class AddressListControllerTest {

  @Autowired
  private AddressListController addressListController;

  @Test
  void getMaxProfitDates(){
//    EnumListDto enumListDto = addressListController.getAddressCityList();
//    assertNotNull(enumListDto.getResult());
//    assertNull(enumListDto.getError());
//    assertEquals("city", enumListDto.getResult().getType());
//    assertEquals(enumListDto.getResult().getStatus(), 200);
//    assertNotNull(enumListDto.getResult().getData());
  }
}
