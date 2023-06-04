package com.holyong.dockingchatservices.infrastructure.im;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Command {

    private Integer code;

    private String nickName;
}
