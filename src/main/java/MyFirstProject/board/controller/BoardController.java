package MyFirstProject.board.controller;

import MyFirstProject.board.dto.BoardDto;
import MyFirstProject.board.dto.BoardSummaryDto;
import MyFirstProject.board.service.BoardService;
import MyFirstProject.constant.SessionConstants;
import MyFirstProject.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
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
        boardService.updateBoardHitCnt(boardIdx);
        model.addAttribute("boardDto",boardDto);
        //게시글 수정권한 확인
        boolean modificationAuthority = boardService.checkModificationAuthority(loginMember.getLoginId(),boardDto.getCreatorId());
        model.addAttribute("modificationAuthority" , modificationAuthority);
        return "board/boardDetail";
    }

    @GetMapping("/board/insertBoard")
    public String openBoardWrite(){
        return "board/boardWrite";
    }

    @PostMapping("/board/insertBoard")
    public String insertBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,BoardDto boardDto) throws Exception{
        boardService.insertBoard(boardDto,loginMember);
        return "redirect:/";
    }

    @PostMapping("/board/deleteBoard")
    public String deleteBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember, int boardIdx) throws Exception {
        log.debug("deleteBoard" + boardIdx);
        boolean delAuthority = boardService.confirmDelAuthority(boardIdx,loginMember);
        if (delAuthority == false){ //비정상 접근 : 작성자와 다른 아이디
            log.debug("비정상 접근 : 작성자와 다른 아이디가 게시글을 삭제하려고 함");
            log.debug("홈으로 리다이렉트 처리함");
            return "redirect:/";
        }

        boardService.deleteBoard(boardIdx);
        return "redirect:/";

    }
    @GetMapping("/board/editBoard")
    public String openEditBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,@RequestParam("board_idx") int boardIdx,Model model) throws Exception {
        log.debug("editBoard" + boardIdx);
        boolean delAuthority = boardService.confirmDelAuthority(boardIdx,loginMember);
        if (delAuthority == false){ //비정상 접근 : 작성자와 다른 아이디
            log.debug("비정상 접근 : 작성자와 다른 아이디가 게시글을 수정하려고 함");
            log.debug("홈으로 리다이렉트 처리함");
            return "redirect:/";
        }

        BoardDto boardDto = boardService.getBoardDetail(boardIdx);
        log.debug(boardDto.getContents());
        model.addAttribute("boardDto",boardDto);
        return "board/boardEdit";
    }
    @PostMapping("/board/editBoard")
    public String editBoard(BoardDto boardDto){
        log.debug("접근");
        //게시글 수정
        try {
            boardService.editBoard(boardDto);
        } catch (Exception e){
            log.debug("게시글 수정 중 오류 발생 : 게시글 번호 : " + boardDto.getBoardIdx());
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/";
    }




}
