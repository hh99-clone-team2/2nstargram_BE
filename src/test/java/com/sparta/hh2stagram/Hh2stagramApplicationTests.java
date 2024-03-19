package com.sparta.hh2stagram;

import com.sparta.hh2stagram.domain.likes.service.LikesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LikesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LikesService likesService;

    @Test
    @WithMockUser(authorities = {"ROLE_USER"}, password = "Aabcd123!", setupBefore = TestExecutionEvent.TEST_METHOD, username = "sanha", value = "user")
    void likeOrUnlikePost() throws Exception {
        // 게시글의 ID와 사용자의 ID를 임의로 설정합니다.
        Long postId = 1L;
        Long userId = 1L;

        // 좋아요 추가 요청을 보냅니다
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{postId}/likes?userId={userId}", postId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("좋아요가 추가되었습니다."))
                .andDo(MockMvcResultHandlers.print());

        // 좋아요 취소 요청을 보냅니다.
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{postId}/likes?userId={userId}", postId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("좋아요가 취소되었습니다."))
                .andDo(MockMvcResultHandlers.print());
    }
}
