package MatchingMaker.MatchingMaker.service;

import MatchingMaker.MatchingMaker.domain.Answerer;
import MatchingMaker.MatchingMaker.domain.Member;
import MatchingMaker.MatchingMaker.dto.AnswererDTO;
import MatchingMaker.MatchingMaker.repository.AnswererRepository;
import MatchingMaker.MatchingMaker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswererService {
    private final AnswererRepository answererRepository;
    private final MemberRepository memberRepository;

    public void registerAnswerer(AnswererDTO answererDTO, String memberEmail) {
        Answerer answerer = new Answerer();
        answerer.setField(answererDTO.getField());
        answerer.setTag(answererDTO.getTag());

        // 파일 저장 로직
        String filePath = answererDTO.saveProofFile();
        answerer.setProofFile(filePath);

        Optional<Member> optionalMember = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMember.isPresent()) {
            answerer.setMember(optionalMember.get());
        } else {
            // 회원 이메일이 DB에 없는 경우의 예외 처리
            throw new IllegalArgumentException("주어진 아이디를 찾을 수 없습니다.");
        }

        answererRepository.save(answerer);
    }
}
