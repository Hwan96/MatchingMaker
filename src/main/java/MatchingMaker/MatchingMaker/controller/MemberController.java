package MatchingMaker.MatchingMaker.controller;

import MatchingMaker.MatchingMaker.domain.Member;
import MatchingMaker.MatchingMaker.domain.Question;
import MatchingMaker.MatchingMaker.dto.MemberDTO;
import MatchingMaker.MatchingMaker.service.MemberService;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    //생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/signUp")
    public String signupForm() { return "signUp"; }

    @PostMapping("/signUp")
    public String signup(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.signup");
        System.out.println("memberDTO = " + memberDTO);
        memberService.signup(memberDTO);
        return "login";
    }
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @PostMapping("/login")
    public  String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            //login 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            //login 실패
            return  "login";
        }
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // model : 어떠한 html로 가져갈 데이터가 있을때 사용
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/list/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/myPage_checkPassword")
    public String passwordCheckForm() { return "myPage_checkPassword"; }

    @PostMapping("/myPage_checkPassword")
    public String passwordCheck(@RequestParam String checkPassword, HttpSession session, Model model) {
        // 현재 로그인한 사용자의 비밀번호를 DB에서 가져와 비교해야 합니다.
        // 이 예제에서는 Email을 사용해 Member를 찾고, 비밀번호를 확인합니다.
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO currentMember = memberService.findByEmail(loginEmail);
        if(currentMember != null && currentMember.getMemberPassword().equals(checkPassword)) {
            model.addAttribute("member", currentMember);
            return "myPage_info";
        }
        model.addAttribute("passwordError", true);
        return "myPage_checkPassword";
    }

    @GetMapping("/myPage_changePassword")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByEmail(myEmail);
        model.addAttribute("member", memberDTO);
        return "myPage_changePassword";
    }

    @PostMapping("/myPage_changePassword")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/myPage_info";
    }

    @GetMapping("/myPage_info")
    public String myInfoForm(HttpSession session, Model model) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO currentMember = memberService.findByEmail(loginEmail);
        model.addAttribute("member", currentMember);
        return "myPage_info";
    }

    @GetMapping("/list/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "home";
    }

    @PostMapping("/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult == null ? "duplicate" : checkResult;
    }

    @GetMapping("/myPage_accountRegister")
    public String accountRegisterForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);;
        return "myPage_accountRegister";
    }

    @PostMapping("/myPage_accountRegister")
    public String accountRegister(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/myPage_accountRegister";
    }

    @GetMapping("/payAndCalculate")
    public String payAndCalculate(HttpSession session, Model model) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        if (memberEmail == null) {
            return "redirect:/login";
        }
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);
        model.addAttribute("points", memberDTO.getPoints());
        return "payAndCalculate";
    }

    @GetMapping("/pay")
    public String pay(HttpSession session, Model model) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        if (memberEmail == null) {
            return "redirect:/login";
        }
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);
        model.addAttribute("points", memberDTO.getPoints());
        return "pay";
    }
    @PostMapping("/pay")
    public String addPoints(HttpSession session, @RequestParam int points) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);
        memberService.addPoints(memberDTO.getId(), points);
        return "redirect:/payAndCalculate";
    }

    @GetMapping("/calculate")
    public String calculate(HttpSession session, Model model) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        if (memberEmail == null) {
            return "redirect:/login";
        }
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);
        model.addAttribute("points", memberDTO.getPoints());
        return "calculate";
    }
    @PostMapping("/calculate")
    public String subtractPoints(HttpSession session, @RequestParam int points, RedirectAttributes redirectAttributes) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);
        try {
            memberService.subtractPoints(memberDTO.getId(), points);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("pointError", "calculate");
            return "redirect:/calculate";
        }
        return "redirect:/payAndCalculate";
    }
}
