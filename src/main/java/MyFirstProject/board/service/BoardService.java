package MyFirstProject.board.service;

import MyFirstProject.board.dto.BoardDto;
import MyFirstProject.board.dto.BoardSummaryDto;
import MyFirstProject.board.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    BoardMapper boardMapper;

    public List<BoardSummaryDto> getBoardSummaryList() throws Exception{
        BoardDto boardDto = boardMapper.selectSummarizedBoardList();
    }
}
