package com.sycoldstorage.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 로그인 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

//    @NotNull(message = "ID를 입력하세요.")
//    @Size(min= 3, message = "ID는 3자리 이상 입력하세요.")
    private String id;

//    @NotNull(message = "Password를 입력하세요.")
//    @Size(min= 4, message = "Password는 4자리 이상 입력하세요.")
    private String password;
}
