package MatchingMaker.MatchingMaker.controller;

import MatchingMaker.MatchingMaker.domain.Question;
import MatchingMaker.MatchingMaker.dto.MemberDTO;
import MatchingMaker.MatchingMaker.dto.QuestionDTO;
import MatchingMaker.MatchingMaker.service.MemberService;
import MatchingMaker.MatchingMaker.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping("/questionRegister")
    public String registerQuestionForm() {
        return "questionRegister";
    }
    @PostMapping("/questionRegister")
    public String registerQuestion(@ModelAttribute QuestionDTO questionDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        // 로그인한 사용자의 이메일 가져오기
        String memberEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);
        int requiredPoints = questionDTO.getRegistrationPoints();

        if (memberDTO.getPoints() >= requiredPoints) {
            memberService.subtractPoints(memberDTO.getId(), requiredPoints);
            questionService.registerQuestion(questionDTO, memberEmail);
            return "redirect:/main";
        } else {
            redirectAttributes.addFlashAttribute("pointError", "register");
            return "redirect:/questionRegister";
        }
    }

    // 로그인한 사용자의 질문 목록을 가져오는 요청 처리
    @GetMapping("myPage_questionList")
    public String getMyQuestion(HttpSession session, Model model) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        if (memberEmail == null) {
            return "redirect:/login";
        }
        List<Question> question = questionService.getQuestionByMemberEmail(memberEmail);
        model.addAttribute("question", question);
        return "myPage_questionList";  // 질문 목록을 보여줄 페이지 이름
    }
}
