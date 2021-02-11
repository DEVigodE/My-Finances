package com.zibmbrazil.myFinances.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class UserDTO {
	private String email;
	private String name;
	private String password;

}
