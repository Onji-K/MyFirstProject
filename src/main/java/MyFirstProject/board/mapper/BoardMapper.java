package MyFirstProject.board.mapper;

import MyFirstProject.board.dto.BoardDto;
import MyFirstProject.board.dto.BoardFileDto;
import MyFirstProject.board.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {

    List<BoardDto> selectSummarizedBoardList() throws Exception;

    BoardDto selectBoardDetailByBoardIdx(int boardIdx) throws Exception;

    void updateBoardHitCnt(int boardIdx) throws Exception;

    void insertBoard(BoardDto boardDto) throws Exception;

    String selectBoardCreatorByBoardIdx(int boardIdx) throws Exception;

    void deleteBoard(int boardIdx) throws Exception;

    void updateBoard(BoardDto boardDto) throws Exception;

    void insertBoardFileList(List<BoardFileDto> list) throws Exception;

    List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;

    String selectStoredFilePath(int idx);

    String selectOriginalFileName(int idx);

    Optional<Integer> selectThumbnailImgIdx(int boardIdx);

    List<CommentDto> selectBoardCommentList(int boardIdx);


    void insertComment(CommentDto commentDto);
}
