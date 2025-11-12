package codesquad.codestagram.controller;

import codesquad.codestagram.dto.LoginForm;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.LoginService;
import codesquad.codestagram.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/users/login")
    public String getLoginForm(@RequestParam(defaultValue = "/") String redirectURL, Model model){
        model.addAttribute("redirectURL", redirectURL);
        return "user/login";
    }


    @PostMapping("/users/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpSession session,
                        @RequestParam(defaultValue = "/") String redirectURL){

        if(bindingResult.hasErrors()){
            return "user/login";
        }

        // 1. id, password 확인
        User user = loginService.login(loginForm.getUserId(), loginForm.getPassword());

        if(user == null){
            bindingResult.reject("login Fail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "user/login";
        }

        // 2. 로그인 성공 -> 세션에 user 등록
        session.setAttribute(SessionConst.LOGIN_USER, user);

        return "redirect:" + redirectURL;
    }

    @PostMapping("users/logout")
    public String logout(HttpSession session){
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }



}
