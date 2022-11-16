package MyFirstProject.board.common;

import MyFirstProject.board.dto.BoardFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class FileUtils {
    public List<BoardFileDto> parseFileInfo(int boardIdx, String creatorId, MultipartHttpServletRequest multipartHttpServletRequest){
        log.debug("fileUtils Log Start\n");
        if (ObjectUtils.isEmpty(multipartHttpServletRequest)){return null;}

        List<BoardFileDto> fileList = new ArrayList<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime current = ZonedDateTime.now();
        String path = "images/" + current.format(format);
        File file = new File(path);
        if(file.exists() == false){
            file.mkdirs();
        }
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        String newFileName, originalFileExtension, contentType;
        while (iterator.hasNext()){
            List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
            for (MultipartFile multipartFile : list){
                if (multipartFile.isEmpty() == false){
                    contentType = multipartFile.getContentType();
                    if (ObjectUtils.isEmpty(contentType)){
                        break;
                    }
                    else {
                        if (contentType.contains("image/jpeg")) {
                            originalFileExtension = ".jpg";
                        } else if (contentType.contains("image/png")) {
                            originalFileExtension = ".png";
                        } else if (contentType.contains("image/gif")) {
                            originalFileExtension = ".gif";
                        } else {
                            break;
                        }
                    }
                    newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
                    BoardFileDto boardFileDto = new BoardFileDto();
                    boardFileDto.setBoardIdx(boardIdx);
                    log.debug("boardIdx : " + boardIdx);
                    boardFileDto.setCreatorId(creatorId);
                    log.debug("creatorId : " + creatorId);
                    boardFileDto.setFileSize(multipartFile.getSize());
                    boardFileDto.setOriginalFileName(multipartFile.getOriginalFilename());
                    log.debug("originalFileName : " + multipartFile.getOriginalFilename());
                    boardFileDto.setStoredFilePath(path + "/" + newFileName);


                    try {
                        log.debug("try to save file : " + newFileName);
                        file = new File(path + "/" + newFileName);
                        multipartFile.transferTo(file);
                    } catch (IOException e){
                        log.error("IOException while save file :" + newFileName);
                        boardFileDto.setHasError(true);
                    }
                    fileList.add(boardFileDto);
                }
            }
        }
        log.debug("fileUtils Log End\n");
        return fileList;
    }

    public void deleteFileList(List<BoardFileDto> list) throws Exception{
        log.debug("deleteFileList execute");
        for (BoardFileDto boardFileDto : list){
            File file = new File(boardFileDto.getStoredFilePath());
            if(file.exists()){
                if (file.delete()){
                    log.debug(boardFileDto.getStoredFilePath() + " -> 삭제 성공");
                } else {
                    log.debug(boardFileDto.getStoredFilePath() + " -> 삭제 실패");
                }
            } else {
                log.debug(boardFileDto.getStoredFilePath() + " -> 파일 존재하지 않음");
            }
        }

    }
}
