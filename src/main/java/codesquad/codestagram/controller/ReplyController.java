package codesquad.codestagram.controller;

import codesquad.codestagram.dto.RequestReplyDto;
import codesquad.codestagram.entity.Reply;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/reply")
    public String writeReply(@ModelAttribute RequestReplyDto requestReplyDto){
        replyService.save(requestReplyDto);
        return "redirect:/articles/" + requestReplyDto.getArticleId();
    }

    @DeleteMapping("reply/{replyId}")
    public String deleteReply(@PathVariable Long replyId, HttpSession session, RedirectAttributes redirectAttributes){

        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        Reply reply = replyService.findByReplyId(replyId);

        if(loginUser.getId().equals(reply.getUser().getId())){
            replyService.delete(reply);
            return "redirect:/articles/" + reply.getArticle().getId();
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 댓글만 삭제할 수 있습니다.");
        return "redirect:/articles/" + reply.getArticle().getId();

    }





}
