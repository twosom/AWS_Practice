package practice.aws.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.common.contenttype.ContentType;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import practice.aws.domain.posts.Posts;
import practice.aws.domain.posts.PostsRepository;
import practice.aws.web.dto.PostsSaveRequestDto;
import practice.aws.web.dto.PostsUpdateRequestDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }



    @Test
    @WithMockUser(roles = "USER")
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
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all).extracting("title").containsExactly(title);
        Assertions.assertThat(all).extracting("content").containsExactly(content);
    }


    @Test
    @WithMockUser(roles = "USER")
    void modifyPostsTest() throws Exception {
        String title = "title";
        String content = "content";
        String author = "two_somang@icloud.com";

        PostsSaveRequestDto saveRequestDto = PostsSaveRequestDto
                .builder()
                .title(title)
                .content(content)
                .author(author)
                .build();


        String url = "http://localhost:" + port + "/api/v1/posts";

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(saveRequestDto)))
                .andExpect(status().isOk());

        /* 수정 시작 */
        Long savedId = postsRepository.findAll().get(0).getId();

        String updateTitle = "title2";
        String updateContent = "content2";

        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto
                .builder()
                .content(updateContent)
                .title(updateTitle)
                .build();
        url = "http://localhost:" + port + "/api/v1/posts/" + savedId;

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        /* 업데이트 됬는지 확인 */
        List<Posts> result = postsRepository.findAll();

        Assertions.assertThat(result).extracting("title").containsExactly(updateTitle);
        Assertions.assertThat(result).extracting("content").containsExactly(updateContent);

    }


}