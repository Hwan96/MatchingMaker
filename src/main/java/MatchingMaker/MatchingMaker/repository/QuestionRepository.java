package MatchingMaker.MatchingMaker.repository;

import MatchingMaker.MatchingMaker.domain.Member;
import MatchingMaker.MatchingMaker.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByMember(Member member);
}
