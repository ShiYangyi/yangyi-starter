package com.example.yangyistarter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

@Builder
@Setter
@Getter
public class UserDTO {

    private Long id;
    @Length(max = 20, message = "用户名不能超过20个字符")
    @Length(min = 3, message = "用户名不能少于3个字符")
    @Pattern(regexp = "^[A-Za-z0-9\\-\\_]*$", message = "用户名只能包含字母、数字、-、_")
    private String name;
    @Length(max = 20, message = "密码不能超过20个字符")
    @Length(min = 6, message = "密码不能少于6个字符")
    private String password;
    private String role;

}
