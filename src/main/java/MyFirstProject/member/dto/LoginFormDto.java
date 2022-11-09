package MyFirstProject.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginFormDto {

//    private String loginForm = "fail";

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;

}
