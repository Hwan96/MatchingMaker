package MatchingMaker.MatchingMaker.repository;

import MatchingMaker.MatchingMaker.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 회원 정보 조회(member_table에 member_email이 존재하는지)
    Optional<Member> findByMemberEmail(String memberEmail);
}
