package codesquad.codestagram.controller;


import codesquad.codestagram.argumentresolver.RequestIp;
import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.dto.ResponseArticleDto;
import codesquad.codestagram.dto.TemporaryArticleResponseDto;
import codesquad.codestagram.entity.Article;
import codesquad.codestagram.entity.Reply;
import codesquad.codestagram.entity.TemporaryArticle;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.service.ArticleService;
import codesquad.codestagram.service.ReplyService;
import codesquad.codestagram.service.TemporaryArticleService;
import codesquad.codestagram.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ArticleController {

    private final TemporaryArticleService temporaryArticleService;
    private final ArticleService articleService;
    private final ReplyService replyService;

    public ArticleController(TemporaryArticleService temporaryArticleService, ArticleService articleService, ReplyService replyService) {
        this.temporaryArticleService = temporaryArticleService;
        this.articleService = articleService;
        this.replyService = replyService;
    }

    @GetMapping("/articles")
    public String getArticleForm(){
        return "qna/form.html";
    }

    @PostMapping("/articles")
    public String writeArticle(@ModelAttribute RequestArticleDto requestArticle) {
        articleService.save(requestArticle);
        return "redirect:/";
    }

    @PostMapping("/articles/recommend/{articleId}")
    public String recommendArticle(@PathVariable Long articleId, HttpSession session) {
        User loginUser = (User)session.getAttribute(SessionConst.LOGIN_USER);
        articleService.recommendArticle(loginUser.getId(), articleId);
        return "redirect:/";
    }

    @PostMapping("/temporaryArticles")
    public String writeTemporaryArticle(@ModelAttribute RequestArticleDto requestArticle) {
        temporaryArticleService.save(requestArticle);
        return "redirect:/";
    }

    @GetMapping("/temporaryArticles")
    public String getTemporaryArticles(HttpSession session, Model model) {
        User loginUser = (User)session.getAttribute(SessionConst.LOGIN_USER);
        TemporaryArticleResponseDto temporaryArticleResponse = temporaryArticleService.findTemporaryArticles(loginUser.getId());
        model.addAttribute("temporaryArticleResponseDto", temporaryArticleResponse);
        return "article/temporaryArticles";
    }

    @GetMapping("/temporaryArticle/{id}")
    public String getTemporaryArticle(@PathVariable Long id, Model model) {
        TemporaryArticle temporaryArticle = temporaryArticleService.findById(id);
        model.addAttribute("temporaryArticle", temporaryArticle);
        return "article/temporaryArticle";
    }

    @GetMapping("/temporaryArticles/edit/{temporaryArticleId}")
    public String showTemporaryArticleEditForm(@PathVariable Long temporaryArticleId,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        TemporaryArticle temporaryArticle = temporaryArticleService.findById(temporaryArticleId);

        if(loginUser.getId().equals(temporaryArticle.getUser().getId())){
            model.addAttribute("temporaryArticle", temporaryArticle);
            return "article/temporaryArticleEdit";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 수정할 수 있습니다.");
        return "redirect:/temporaryArticles/" + temporaryArticleId;
    }

    @PutMapping("/temporaryArticles/{temporaryArticleId}")
    public String editTemporaryArticle(@ModelAttribute RequestArticleDto editArticleInfo,
                              @PathVariable Long temporaryArticleId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        TemporaryArticle temporaryArticle = temporaryArticleService.findById(temporaryArticleId);

        if(loginUser.getId().equals(temporaryArticle.getUser().getId())){
            temporaryArticleService.edit(temporaryArticleId, editArticleInfo);
            return "redirect:/temporaryArticles/";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 수정할 수 있습니다.");
        return "redirect:/temporaryArticle/" + temporaryArticleId;
    }

    @DeleteMapping("/temporaryArticles/{temporaryArticleId}")
    public String deleteTemporaryArticle(@PathVariable Long temporaryArticleId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes){
        TemporaryArticle temporaryArticle = temporaryArticleService.findById(temporaryArticleId);
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        if(temporaryArticle.getUser().getId().equals(loginUser.getId())){
            temporaryArticleService.delete(temporaryArticle);
            return "redirect:/temporaryArticles";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "본인이 작성한 글만 삭제할 수 있습니다.");
        return "redirect:/temporaryArticle/" + temporaryArticleId;
    }


    @GetMapping("/articles/edit/{articleId}")
    public String showArticleEditForm(@PathVariable Long articleId,
                                      Model model,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

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
