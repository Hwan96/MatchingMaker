package MatchingMaker.MatchingMaker.controller;

import MatchingMaker.MatchingMaker.dto.AnswererDTO;
import MatchingMaker.MatchingMaker.service.AnswererService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AnswererController {
    private final AnswererService answererService;

    @GetMapping("/answererRegister")
    public String registerAnswererForm() {
        return "answererRegister";
    }

    @PostMapping("/answererRegister")
    public String registerAnswerer(@ModelAttribute AnswererDTO answererDTO, HttpSession session) {
        String memberEmail = (String) session.getAttribute("loginEmail");
        if (memberEmail == null) {
            return "login";
        }
        answererService.registerAnswerer(answererDTO, memberEmail);
        return "main";
    }

    @GetMapping("/myPage_answerList")
    public String answerList() { return "myPage_answerList"; }
}
