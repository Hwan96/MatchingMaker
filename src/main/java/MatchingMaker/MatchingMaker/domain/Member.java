package MatchingMaker.MatchingMaker.domain;

import MatchingMaker.MatchingMaker.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "member_table")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    @Column
    private String nickName;

    @Column
    private String gender;

    @Column
    private LocalDate birthDate;

    @Column
    private String phoneNumber;

    @Column
    private String bankName;

    @Column
    private String accountNumber;

    @Column
    private String accountHolder;

    @Column
    private int points = 0;

    public static Member toMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberPassword(memberDTO.getMemberPassword());
        member.setMemberName(memberDTO.getMemberName());
        member.setNickName(memberDTO.getNickName());
        member.setGender(memberDTO.getGender());
        member.setBirthDate(memberDTO.getBirthDate());
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setBankName(memberDTO.getBankName());
        member.setAccountNumber(memberDTO.getAccountNumber());
        member.setAccountHolder(memberDTO.getAccountHolder());
        member.setPoints(memberDTO.getPoints());
        return member;
    }
    public static Member toUpdateMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setId(memberDTO.getId());
        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberPassword(memberDTO.getMemberPassword());
        member.setMemberName(memberDTO.getMemberName());
        member.setNickName(memberDTO.getNickName());
        member.setGender(memberDTO.getGender());
        member.setBirthDate(memberDTO.getBirthDate());
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setBankName(memberDTO.getBankName());
        member.setAccountNumber(memberDTO.getAccountNumber());
        member.setAccountHolder(memberDTO.getAccountHolder());
        return member;
    }
}
