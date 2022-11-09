package MyFirstProject.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JoinFormDto {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
}
