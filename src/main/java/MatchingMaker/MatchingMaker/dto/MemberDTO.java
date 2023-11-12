package MatchingMaker.MatchingMaker.dto;

import MatchingMaker.MatchingMaker.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String nickName;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private int points;

    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setMemberEmail(member.getMemberEmail());
        memberDTO.setMemberPassword(member.getMemberPassword());
        memberDTO.setMemberName(member.getMemberName());
        memberDTO.setNickName(member.getNickName());
        memberDTO.setGender(member.getGender());
        memberDTO.setBirthDate(member.getBirthDate());
        memberDTO.setPhoneNumber(member.getPhoneNumber());
        memberDTO.setBankName(member.getBankName());
        memberDTO.setAccountNumber(member.getAccountNumber());
        memberDTO.setAccountHolder(member.getAccountHolder());
        memberDTO.setPoints(member.getPoints());
        return memberDTO;
    }
}
