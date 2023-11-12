package MatchingMaker.MatchingMaker.domain;

import MatchingMaker.MatchingMaker.dto.QuestionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Entity
@Setter
@Getter
@Table(name = "question_table")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String file;

    @Column
    private String field;

    @Column
    private String tags;

    @Column
    private LocalDateTime expectedAnswerTime;

    @Column
    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Question fromQuestionDto(QuestionDTO questionDTO) {
        System.out.println("Answer Time Type: " + questionDTO.getAnswerTimeType());
        System.out.println("Specific Answer Time: " + questionDTO.getSpecificAnswerTime());
        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());
        question.setFile(questionDTO.getFilePath());
        question.setField(questionDTO.getField());
        question.setTags(questionDTO.getTags());
        question.setExpectedAnswerTime(LocalDateTime.now()); // 모든 질문을 '실시간'으로 설정
        /*
        if(questionDTO.getAnswerTimeType() != null && questionDTO.getAnswerTimeType().equals("specificTime")) {
            // questionDTO에서 시간 정보를 받아 옵니다.
            LocalTime specificTime = questionDTO.getSpecificAnswerTime();

            // 현재 날짜와 specificTime을 결합하여 LocalDateTime을 만듭니다.
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), specificTime);
            question.setExpectedAnswerTime(dateTime);
        } else {
            question.setExpectedAnswerTime(LocalDateTime.now());
        }
        */

        question.setPoints(questionDTO.getRegistrationPoints());
        return question;
    }
}
