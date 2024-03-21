package com.sparta.hh2stagram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto;
import com.sparta.hh2stagram.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@SpringBootTest
class Hh2stagramApplicationTests {

//    @Test
//    void contextLoads() {
//    }

//    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser // 인증된 사용자를 모의로 생성
    public void testCreatePost() throws Exception {
        // 테스트용 요청 DTO 생성
        PostRequestDto.CreatePostRequestDto requestDto = new PostRequestDto.CreatePostRequestDto();
        requestDto.setContents("테스트 게시물");

        // 파일을 업로드하기 위한 MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("files", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());

        // MockMvc를 사용하여 HTTP 요청 모의
        mockMvc.perform(MockMvcRequestBuilders.multipart("/posts")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(objectMapper.writeValueAsString(requestDto)))
                // 응답 검증
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}
