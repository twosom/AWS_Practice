package practice.aws.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import practice.aws.config.auth.LoginUser;
import practice.aws.config.auth.dto.SessionUser;
import practice.aws.repository.PostsQueryRepository;
import practice.aws.service.PostsService;
import practice.aws.web.dto.PostsListResponseDto;
import practice.aws.web.dto.PostsResponseDto;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;
    private final PostsQueryRepository postsQueryRepository;


    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        List<PostsListResponseDto> posts = postsQueryRepository.findAllDesc();
        model.addAttribute("posts", posts);

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts/save";
    }


    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts/update";
    }
}
