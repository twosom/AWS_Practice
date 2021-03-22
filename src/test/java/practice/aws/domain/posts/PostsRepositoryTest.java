package practice.aws.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Test
    void loadPosts() throws Exception {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(
                Posts.builder()
                        .title(title)
                        .content(content)
                        .author("two_somang@icloud.com")
                        .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Assertions.assertThat(postsList).extracting("title").containsExactly(title);
        Assertions.assertThat(postsList).extracting("content").containsExactly(content);

    }

    @Test
    void baseTimeEntityTest() throws Exception {
        //given
        Posts posts = Posts
                .builder()
                .title("title")
                .content("content")
                .author("author")
                .build();
        postsRepository.save(posts);

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        for (Posts findPost : postsList) {
            System.out.println("findPost = " + findPost);
            System.out.println("findPost.getCreatedDate() = " + findPost.getCreatedDate());
        }

    }

}