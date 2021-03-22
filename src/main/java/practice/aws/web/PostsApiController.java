package practice.aws.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import practice.aws.service.PostsService;
import practice.aws.web.dto.PostsResponseDto;
import practice.aws.web.dto.PostsSaveRequestDto;
import practice.aws.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/posts")
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping
    public Long save(@RequestBody PostsSaveRequestDto postsSaveRequestDto) {
        log.info("saveDTO = {}", postsSaveRequestDto);
        return postsService.save(postsSaveRequestDto);
    }

    @PutMapping("/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }
}
