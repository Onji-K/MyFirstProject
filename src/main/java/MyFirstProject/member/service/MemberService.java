package MyFirstProject.member.service;

import MyFirstProject.member.dto.JoinFormDto;
import MyFirstProject.member.dto.LoginFormDto;
import MyFirstProject.member.dto.MemberDto;
import MyFirstProject.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    /**
     *
     * @return 성공하면 MemberDto 인스턴스를 반환하고, 실패하면 null 반환
     */
    public MemberDto login(LoginFormDto loginFormDto){
        //추후 비밀번호를 암호화 할 예정이라 아래와 같이 만듬
        MemberDto memberDto = memberMapper.findMemberByIdPw(loginFormDto);
        return memberDto;
    }

    @Transactional
    public int join(JoinFormDto joinFormDto) {
        MemberDto idOwner = memberMapper.findMemberById(joinFormDto.getLoginId());
        if (idOwner != null){ //이미 멤버가 존재
            return 0;
        }
        int updateCount = memberMapper.insertMember(joinFormDto);
        return updateCount;
    }
}
