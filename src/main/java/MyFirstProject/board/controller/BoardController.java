package MyFirstProject.board.controller;

import MyFirstProject.board.dto.BoardDto;
import MyFirstProject.board.dto.BoardSummaryDto;
import MyFirstProject.board.service.BoardService;
import MyFirstProject.constant.SessionConstants;
import MyFirstProject.member.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class BoardController {
    @Autowired
    BoardService boardService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,
                       Model model) throws Exception{
        if (loginMember == null){ //세션 없으면
            return "home/logoutHome";
        }


        //세션 있으면
        model.addAttribute("member",loginMember);

        //게시글이 있으면
        List<BoardSummaryDto> boardSummaryList=  boardService.getBoardSummaryList();
        model.addAttribute("list",boardSummaryList);

        return "home/loginHome";
    }

    @GetMapping("/board/openBoardDetail")
    public String boardDetail(@RequestParam("board_idx") int boardIdx,Model model,@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember) throws Exception{
        if (loginMember == null){ //세션 없으면
            return "redirect:/";
        }


        //로그인 정보 제공 -> 사이드 바 위해서
        model.addAttribute("member",loginMember);
        //게시글 상세 내용 가져오기
        BoardDto boardDto =  boardService.getBoardDetail(boardIdx);
        model.addAttribute("boardDto",boardDto);
        //게시글 수정권한 확인
        boolean modificationAuthority = boardService.checkModificationAuthority(loginMember.getLoginId(),boardDto.getCreatorId());
        System.out.println(modificationAuthority);
        model.addAttribute("modificationAuthority" , modificationAuthority);
        return "board/boardDetail";
    }

    @GetMapping("/board/insertBoard")
    public String openBoardWrite(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,Model model){
        if (loginMember == null) { //세션 없으면
            return "redirect:/";
        }
        model.addAttribute("member",loginMember);
        return "board/boardWrite";
    }

    @PostMapping("/board/insertBoard")
    public String insertBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,BoardDto boardDto,Model model) throws Exception{
        if (loginMember == null) { //세션 없으면
            return "redirect:/";
        }

        boardService.insertBoard(boardDto,loginMember);
        model.addAttribute("member",loginMember);
        return "redirect:/";

    }



}
