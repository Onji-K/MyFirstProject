package MyFirstProject.board.dto;

import lombok.Data;

@Data
public class BoardSummaryDto {
    private int boardIdx;
    private String title;
    private int hitCount;
    private String summary;
    private String editTimeInfo;
    private int thumbnailImageIdx;
}
