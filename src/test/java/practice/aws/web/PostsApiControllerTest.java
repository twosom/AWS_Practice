package practice.aws.web;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import practice.aws.domain.posts.Posts;
import practice.aws.domain.posts.PostsRepository;
import practice.aws.web.dto.PostsSaveRequestDto;
import practice.aws.web.dto.PostsUpdateRequestDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PostsApiControllerTest {

    @Autowired
    EntityManager em;

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PostsRepository postsRepository;

    @Test
    void savePostsTest() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String author = "two_somang@icloud.com";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all).extracting("title").containsExactly(title);
        Assertions.assertThat(all).extracting("content").containsExactly(content);
        Assertions.assertThat(all).extracting("author").containsExactly(author);
    }


    @Test
    void modifyPostsTest() throws Exception {
        //테스트 케이스 시나리오 : 1. /api/v1/posts POST 방식으로 저장
        //                   2. /api/v1/posts/id PUT 방식으로 수정
        /* 저장 */
        String originTitle = "title";
        String originContent = "content";
        String author = "author";

        PostsSaveRequestDto saveRequestDto = PostsSaveRequestDto
                .builder()
                .title(originTitle)
                .content(originContent)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";
        ResponseEntity<Long> saveResponseEntity
                = restTemplate.postForEntity(url, saveRequestDto, Long.class);
        /* 저장 결과 검증 */
        Assertions.assertThat(saveResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(saveResponseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all).extracting("title").containsExactly(originTitle);
        Assertions.assertThat(all).extracting("content").containsExactly(originContent);

        em.flush();
        em.clear();

        /* 수정 */
        String newTitle = "title2";
        String newContent = "content2";

        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto
                .builder()
                .title(newTitle)
                .content(newContent)
                .build();

        Long savedId = saveResponseEntity.getBody();
        url = "http://localhost:" + port + "/api/v1/posts/" + savedId;

        ResponseEntity<Long> updateResponseEntity
                = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequestDto), Long.class);

        Assertions.assertThat(updateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(updateResponseEntity.getBody()).isGreaterThan(0L);

        List<Posts> result = postsRepository.findAll();
        Assertions.assertThat(result).extracting("title").containsExactly(newTitle);
        Assertions.assertThat(result).extracting("content").containsExactly(newContent);
    }




}