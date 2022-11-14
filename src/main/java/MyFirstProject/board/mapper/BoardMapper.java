package MyFirstProject.board.mapper;

import MyFirstProject.board.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDto> selectSummarizedBoardList() throws Exception;

    BoardDto selectBoardDetailByBoardIdx(int boardIdx) throws Exception;

    void updatedBoardHitCnt(int boardIdx) throws Exception;

    void insertBoard(BoardDto boardDto) throws Exception;
}
