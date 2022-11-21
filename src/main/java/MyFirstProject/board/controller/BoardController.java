package MyFirstProject.board.controller;

import MyFirstProject.board.dto.BoardDto;
import MyFirstProject.board.dto.BoardSummaryDto;
import MyFirstProject.board.dto.CommentDto;
import MyFirstProject.board.service.BoardService;
import MyFirstProject.constant.SessionConstants;
import MyFirstProject.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
        //댓글 가져오기
        List<CommentDto> commentList = boardService.getBoardCommentList(boardIdx);
        model.addAttribute("commentList", commentList);



        //게시글 수정권한 확인
        boolean modificationAuthority = boardService.checkModificationAuthority(loginMember.getLoginId(),boardDto.getCreatorId());
        model.addAttribute("modificationAuthority" , modificationAuthority);
        return "board/boardDetail";
    }

    @PostMapping("/board/comment/add")
    public String addComment(Model model , HttpServletRequest request, @RequestParam Map<String, Object> paramMap){
        MemberDto member = (MemberDto) request.getSession().getAttribute(SessionConstants.LOGIN_MEMBER);
        int boardIdx = Integer.valueOf(paramMap.get("boardIdx").toString());
        String content = paramMap.get("content").toString();
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(content);
        commentDto.setBoardIdx(boardIdx);
        commentDto.setMemberIdx(member.getMemberIdx());

        //DB에 댓글 넣기
        log.debug("addComment : " + commentDto.getContent() + " By " + commentDto.getMemberDto().getName());
        boardService.addComment(commentDto);

        //모델에 댓글 리스트 추가
        List<CommentDto> commentList = boardService.getBoardCommentList(boardIdx);
        model.addAttribute("commentList", commentList);

        return "board/boardDetail"



    }

    @ResponseBody
    @GetMapping("/board/image")
    public Resource showImage(@RequestParam("img_idx")int idx) throws MalformedURLException {
        log.debug("showImage idx : " + idx);
        String storedFilePath = boardService.getStoredFilePath(idx);
        log.debug(storedFilePath);
        File file = new File(storedFilePath);
        return new UrlResource("file:" + file.getAbsolutePath());
    }

    @ResponseBody
    @GetMapping("/board/getImage")
    public ResponseEntity<Resource> getImage(@RequestParam("img_idx")int idx) throws MalformedURLException{
        log.debug("getImage idx : " + idx);
        //Body - resource
        String storedFilePath = boardService.getStoredFilePath(idx);
        UrlResource resource = new UrlResource("file:"+storedFilePath);
        //Header - encoding
        String originalFileName = boardService.getOriginalFileName(idx);
        String encodedOriginalFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                .body(resource);
    }


    @GetMapping("/board/insertBoard")
    public String openBoardWrite(){
        return "board/boardWrite";
    }

    @PostMapping("/board/insertBoard")
    public String insertBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember,
                              BoardDto boardDto,
                              MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        boardService.insertBoard(boardDto,loginMember, multipartHttpServletRequest);
        return "redirect:/";
    }

    @PostMapping("/board/deleteBoard")
    public String deleteBoard(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER,required = false)MemberDto loginMember, int boardIdx) throws Exception {
        log.debug("deleteBoard : " + boardIdx);
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
