package MyFirstProject.member.dto;

import lombok.Data;

@Data
public class MemberDto {

    private long memberIdx;
    private String loginId;
    private String password;
    private String name;
    //deleted_yn 존재
}
