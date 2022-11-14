package MyFirstProject.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDto {
    private int boardIdx;
    private String title;
    private String contents;
    private int hitCnt;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
    private String creatorName;
    private String creatorId;
}
