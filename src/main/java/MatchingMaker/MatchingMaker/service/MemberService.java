package MatchingMaker.MatchingMaker.service;

import MatchingMaker.MatchingMaker.domain.Member;
import MatchingMaker.MatchingMaker.dto.MemberDTO;
import MatchingMaker.MatchingMaker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signup(MemberDTO memberDTO) {
        /*
           1. dto -> entity(domain) 변환
           2. repository의 save메서드 호출
         */
        Member member = Member.toMember(memberDTO);
        memberRepository.save(member);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<Member> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            Member member = byMemberEmail.get();
            if (member.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(member);
                return dto;
            } else {
                // 비밀번호 불일치(로그인실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (Member member : memberList) {
            memberDTOList.add(MemberDTO.toMemberDTO(member));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMember.get());
        } else {
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<Member> optionalMember = memberRepository.findByMemberEmail(myEmail);
        if (optionalMember.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMember.get());
        } else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(Member.toUpdateMember(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<Member> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        return byMemberEmail.isPresent() ? "duplicate" : "ok";
    }

    public MemberDTO findByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByMemberEmail(email);
        return optionalMember.map(MemberDTO::toMemberDTO).orElse(null);
    }

    public void addPoints(Long memberId, int points) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        memberOpt.ifPresent(member -> {
            member.setPoints(member.getPoints() + points);
            memberRepository.save(member);
        });
    }

    public void subtractPoints(Long memberId, int points) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        memberOpt.ifPresent(member -> {
            int currentPoints = member.getPoints();
            if (currentPoints >= points) {
                member.setPoints(currentPoints - points);
                memberRepository.save(member);
            } else {
                throw new IllegalArgumentException("포인트가 부족합니다.");
            }
        });
    }
}
