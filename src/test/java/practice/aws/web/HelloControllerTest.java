package practice.aws.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
class HelloControllerTest {


    /**
     * Web API를 테스트할 때 사용한다.<br/>
     * 스프링 MVC 테스트의 시작점이다.<br/>
     * 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트가 가능.
     */
    @Autowired
    private MockMvc mvc;

    @Test
    void helloReturn() throws Exception {
        String hello = "hello";


        /* MockMvc를 통해 /hello 주소로 get 요청을 보낸다. (MockMvcRequestBuilders.get())
         *  체이닝이 지원되어 여러 검증을 이어서 선언할 수 있다.
         *
         *  .andExpect(status().isOk()) : mvc.perform 의 결과 검증,
         *  HttpHeader의 Status 를 검증한다.
         * */
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    void helloDtoReturn() throws Exception {
        //given
        String name = "hello";
        int amount = 1000;

        //when
        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

        //then

    }
}