package MyFirstProject.board.service;

import MyFirstProject.board.dto.BoardDto;
import MyFirstProject.board.dto.BoardSummaryDto;
import MyFirstProject.board.mapper.BoardMapper;
import MyFirstProject.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class BoardService {

    @Autowired
    BoardMapper boardMapper;

    public List<BoardSummaryDto> getBoardSummaryList() throws Exception{
        //BoardDto 가져오기
        List<BoardDto> summarizedBoardDto = boardMapper.selectSummarizedBoardList();
        if (summarizedBoardDto.size() == 0){ //게시글이 없으면
            return null;
        }
        //루프 돌면서 요약된 Dto 만들기
        Iterator<BoardDto> iterator = summarizedBoardDto.iterator();
        BoardSummaryDto[] boardSummaryArray = new BoardSummaryDto[summarizedBoardDto.size()];
        int i = 0;
        while (iterator.hasNext()){
            BoardDto board = iterator.next();
            BoardSummaryDto boardSummary = new BoardSummaryDto();

            boardSummary.setBoardIdx(board.getBoardIdx());
            boardSummary.setTitle(board.getTitle());
            boardSummary.setHitCount(board.getHitCnt());
            
            //내용 간추리기
            String ogContents = board.getContents();
            boardSummary.setSummary(ogContents.substring(0,Math.min(100,ogContents.length())));

            //마지막 시간 계산하기
            String finalPhrase;
            String suffix = "Last updated ";
            LocalDateTime lastTime = (board.getUpdatedDatetime() == null) ? board.getCreatedDatetime() : board.getUpdatedDatetime();
            LocalDateTime now = LocalDateTime.now();

            //일단 걸린 시간을 Day 단위 로 구해서 어느 단위로 할 것인지 선택
            Period period = Period.between(lastTime.toLocalDate(),now.toLocalDate());

            if (period.getDays() > 0) {
                //1일 이상
                if (period.getYears() > 0){ //년 단위
                    finalPhrase = suffix + period.getYears() + " years ago";
                } else if (period.getMonths() > 0 ) { //월 단위
                    finalPhrase = suffix + period.getMonths() + " month ago";
                } else { //일 단위
                    finalPhrase = suffix + period.getDays() + " days ago";
                }
            } else {
                //1일 미만
                Duration duration = Duration.between(lastTime,now);
                long seconds = duration.getSeconds();
                if (seconds<60){ //초 단위
                    finalPhrase = suffix + seconds + " seconds ago";
                } else if (seconds<3600) { //분 단위
                    finalPhrase = suffix + seconds/60 + " minutes ago";
                } else{ //시간 단위
                    finalPhrase = suffix + seconds/3600 + " hours ago";
                }
            }
            boardSummary.setEditTimeInfo(finalPhrase);
            boardSummaryArray[i++] = boardSummary;
        }

        return Arrays.stream(boardSummaryArray).toList();
    }

    public BoardDto getBoardDetail(int boardIdx) throws Exception {
        boardMapper.updatedBoardHitCnt(boardIdx);
        BoardDto boardDto = boardMapper.selectBoardDetailByBoardIdx(boardIdx);
        return boardDto;
    }

    public boolean checkModificationAuthority(String loginId, String creatorId) throws Exception {
        return loginId.equals(creatorId);
    }

    public void insertBoard(BoardDto boardDto, MemberDto loginMember) throws Exception {
        boardDto.setCreatorName(loginMember.getName());
        boardDto.setCreatorId(loginMember.getLoginId());
        boardMapper.insertBoard(boardDto);
    }
}