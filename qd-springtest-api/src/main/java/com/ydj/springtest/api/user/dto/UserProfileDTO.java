package com.ydj.springtest.api.user.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserProfileDTO implements Serializable {

    private Integer uid;
    private String userName;
}
