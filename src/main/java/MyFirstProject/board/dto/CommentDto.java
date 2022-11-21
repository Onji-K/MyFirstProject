package MyFirstProject.board.dto;

import MyFirstProject.member.dto.MemberDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int idx;
    private int boardIdx;
    private long memberIdx;
    private String content;
    private LocalDateTime createdDatetime;
    private LocalDateTime updatedDatetime;
    private MemberDto memberDto;
}
