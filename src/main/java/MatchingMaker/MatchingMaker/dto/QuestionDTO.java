package MatchingMaker.MatchingMaker.dto;

import MatchingMaker.MatchingMaker.domain.Question;
import lombok.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuestionDTO {
    private Long id;
    private String title;
    private String content;
    private String field; // 질문 분야
    private String tags; // 질문 태그
    private MultipartFile file; // 받은 파일
    private String answerTimeType = "realTime"; // '실시간' 또는 '시간 선택'
    private LocalTime specificAnswerTime; // 시간 선택시 특정 시간
    private int registrationPoints; // 등록 포인트

    // 파일을 디스크에 저장하고 그 경로를 반환
    public String getFilePath() {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // 파일명을 가져옴
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        try {
            // 파일을 디스크에 저장
            String directory = "D:/MM/Question/"; // 디렉토리 경로 설정
            Path directoryPath = Paths.get(directory);
            if (Files.notExists(directoryPath)) { Files.createDirectories(directoryPath); }
            Path path = directoryPath.resolve(fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString(); // 파일 경로 반환

        } catch (IOException e) {
            // 파일 저장 실패시 적절한 처리가 필요
            e.printStackTrace();
            return null;
        }
    }
    public static Question fromQuestionDto(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());
        question.setFile(questionDTO.getFilePath());
        question.setField(questionDTO.getField());
        question.setTags(questionDTO.getTags());
        if(questionDTO.getAnswerTimeType().equals("specific")) {
            question.setExpectedAnswerTime(questionDTO.getSpecificAnswerTime().atDate(LocalDate.now()));
        }
        question.setPoints(questionDTO.getRegistrationPoints());
        return question;
    }
}
