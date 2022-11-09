package MyFirstProject.board.mapper;

import MyFirstProject.board.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    public BoardDto selectSummarizedBoardList() throws Exception;
}
