package MyFirstProject.member.controller;

import MyFirstProject.constant.SessionConstants;
import MyFirstProject.member.dto.JoinFormDto;
import MyFirstProject.member.dto.LoginFormDto;
import MyFirstProject.member.dto.MemberDto;
import MyFirstProject.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/loginForm")
    public String loginForm(@ModelAttribute LoginFormDto loginFormDto){
        return "home/login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute @Validated LoginFormDto loginFormDto,
                      BindingResult bindingResult,
                      HttpServletRequest request){
        log.debug("/login post");

        
        //입력 값 검사
        if (bindingResult.hasErrors()){
            log.debug("입력값에 문제가 있습니다.");
            return "home/login/loginForm";
        }
        //로그인 멤버 불러오기
        MemberDto loginMember = memberService.login(loginFormDto);
        
        //로그인 실패
        if (loginMember == null){
            log.debug("로그인 실패");
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "home/login/parentRedirect";
        }

        log.debug("로그인 성공");

        //로그인 성공처리
        HttpSession session = request.getSession(true); //세션 없으면 생성
        session.setAttribute(SessionConstants.LOGIN_MEMBER,loginMember);
        return "home/login/ParentRedirect";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }
        return "redirect:/";
    }


    @GetMapping("/joinForm")
    public String joinForm(@ModelAttribute JoinFormDto joinFormDto){
        return "home/login/joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute @Validated JoinFormDto joinFormDto,
                       BindingResult bindingResult){
        log.debug("join post");
        //입력 검증
        if (bindingResult.hasErrors()){
            log.debug("hasErrors send joinForm");
            return "home/login/joinForm";
        }
        //회원가입 시도
        int updateCount = memberService.join(joinFormDto);
        log.debug("업데이트 갯수 : " + updateCount);
        
        //회원가입 결과 체크
        if (updateCount == 0){ //회원가입 실패
            log.debug("회원가입 실패");
            bindingResult.reject("joinFail","이미 사용된 아이디 입니다.");
            return "home/login/joinForm";
        }
        
        //회원가입 성공
        log.debug("회원가입 성공");
        return "home/login/joinSuccess";
    }
}
