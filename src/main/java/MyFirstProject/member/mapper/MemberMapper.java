package MyFirstProject.member.mapper;

import MyFirstProject.member.dto.JoinFormDto;
import MyFirstProject.member.dto.LoginFormDto;
import MyFirstProject.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    MemberDto findMemberByIdPw(LoginFormDto loginFormDto);


    MemberDto findMemberById(String loginId);

    MemberDto findMemberByIdx(int memberIdx);

    int insertMember(JoinFormDto joinFormDto);
}
