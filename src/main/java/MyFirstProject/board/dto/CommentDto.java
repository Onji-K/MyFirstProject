package MyFirstProject.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int idx;
    private int memberIdx;
    private String content;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
}
