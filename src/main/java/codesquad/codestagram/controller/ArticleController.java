package codesquad.codestagram.controller;


import codesquad.codestagram.argumentresolver.RequestIp;
import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.dto.ResponseArticleDto;
import codesquad.codestagram.entity.Article;
import codesquad.codestagram.entity.Reply;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final ReplyService replyService;

    public ArticleController(ArticleService articleService, ReplyService replyService) {
        this.articleService = articleService;
        this.replyService = replyService;
    }

    @GetMapping("/articles")
    public String getArticleForm(HttpSession session){
        User loginUser = (User)session.getAttribute(SessionConst.LOGIN_USER);
        if(loginUser == null){
            return "user/login";
        }
        return "qna/form.html";
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute RequestArticleDto requestArticle, HttpSession session) {
        User loginUser = (User)session.getAttribute(SessionConst.LOGIN_USER);
        if(loginUser == null){
            return "user/login";
        }

        articleService.save(requestArticle);
        return "redirect:/";
    }

    @GetMapping("/articles/edit/{articleId}")
    public String showArticleEditForm(@PathVariable Long articleId,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        if(loginUser == null){
            return "user/login";
        }

        Article article = articleService.findById(articleId);

        if(loginUser.getId().equals(article.getUser().getId())){
            model.addAttribute("article", article);
            return "article/edit";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 수정할 수 있습니다.");
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@ModelAttribute RequestArticleDto editArticleInfo,
                              @PathVariable Long articleId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);
        if(loginUser == null){
            return "user/login";
        }

        Article article = articleService.findById(articleId);

        if(loginUser.getId().equals(article.getUser().getId())){
            articleService.edit(articleId, editArticleInfo);
            return "redirect:/articles/" + articleId;
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 수정할 수 있습니다.");
        return "redirect:/articles/edit/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes){
        Article article = articleService.findById(articleId);
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        if(loginUser == null){
            return "user/login";
        }

        if(article.getUser().getId().equals(loginUser.getId())){
            articleService.delete(article);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 삭제할 수 있습니다.");
        return "redirect:/articles/" + articleId;
    }


    @GetMapping("/")
    public String showArticles(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Article> articleList = articleService.findAll(page);

        model.addAttribute("articles", articleList.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", articleList.getTotalPages());

        return "article/index";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, @RequestIp String clientIp, @RequestParam(defaultValue = "0") int page, Model model){
        ResponseArticleDto article = articleService.findSingleArticle(id, clientIp);
        Page<Reply> replyList = replyService.findByArticleId(id, page);

        model.addAttribute("article", article.getArticle());
        model.addAttribute("replies", replyList.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", replyList.getTotalPages());

        return "article/show";
    }

}
