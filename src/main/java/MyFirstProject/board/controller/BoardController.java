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
                       Model model){
        //home 분기
        if (loginMember == null){
            return "home/logoutHome";
        }
        return "redirect:/board/boardList";
    }

    @GetMapping("/board/boardList")
    public String openBoardList(Model model) throws Exception {
        List<BoardSummaryDto> boardSummaryList=  boardService.getBoardSummaryList();
        model.addAttribute("list",boardSummaryList);
        return "home/loginHome";
    }

    @GetMapping("/board/openBoardDetail")
    public String boardDetail(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,@RequestParam("board_idx") int boardIdx,Model model) throws Exception{
        //게시글 상세 내용 가져오기
        BoardDto boardDto =  boardService.getBoardDetail(boardIdx);
        model.addAttribute("boardDto",boardDto);
        //게시글 수정권한 확인
        boolean modificationAuthority = boardService.checkModificationAuthority(loginMember.getLoginId(),boardDto.getCreatorId());
        model.addAttribute("modificationAuthority" , modificationAuthority);
        return "board/boardDetail";
    }

    @GetMapping("/board/insertBoard")
    public String openBoardWrite(Model model){
        return "board/boardWrite";
    }

    @PostMapping("/board/insertBoard")
    public String insertBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,BoardDto boardDto,Model model) throws Exception{
        boardService.insertBoard(boardDto,loginMember);
        return "redirect:/";
    }



}
