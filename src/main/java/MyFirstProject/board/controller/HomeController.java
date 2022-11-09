package MyFirstProject.board.controller;

import MyFirstProject.board.dto.BoardSummaryDto;
import MyFirstProject.board.service.BoardService;
import MyFirstProject.constant.SessionConstants;
import MyFirstProject.member.dto.JoinFormDto;
import MyFirstProject.member.dto.LoginFormDto;
import MyFirstProject.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    BoardService boardService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,
                             ModelAndView modelAndView) throws Exception{
        if (loginMember == null){ //세션 없으면
            return "home/logoutHome";
        }


        //세션 있으면
        List<BoardSummaryDto> boardSummaryList=  boardService.getBoardSummaryList();

        return "home/loginHome";
    }



}
