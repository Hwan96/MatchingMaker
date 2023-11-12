package MatchingMaker.MatchingMaker.service;

import MatchingMaker.MatchingMaker.domain.Member;
import MatchingMaker.MatchingMaker.domain.Question;
import MatchingMaker.MatchingMaker.dto.MemberDTO;
import MatchingMaker.MatchingMaker.dto.QuestionDTO;
import MatchingMaker.MatchingMaker.repository.MemberRepository;
import MatchingMaker.MatchingMaker.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    public void registerQuestion(QuestionDTO questionDTO, String memberEmail) {
        Question question = Question.fromQuestionDto(questionDTO);

        Optional<Member> optionalMember = memberRepository.findByMemberEmail(memberEmail);
        optionalMember.ifPresent(question::setMember);

        questionRepository.save(question);
    }

    public List<Question> getQuestionByMemberEmail(String memberEmail) {
        Optional<Member> optionalMember = memberRepository.findByMemberEmail(memberEmail);
        return optionalMember.map(member -> questionRepository.findAllByMember(member)).orElse(Collections.emptyList());
    }
}

