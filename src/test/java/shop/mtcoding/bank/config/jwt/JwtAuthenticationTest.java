package shop.mtcoding.bank.config.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyEntity;
import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.UserReqDto.LoginReqDto;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthenticationTest extends DummyEntity {

    private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
    @Autowired
    private MockMvc mvc; //웹 요청을 위해서 필요

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() { //메서드 시작 전 실행되는것
        // User user = User.builder()
        //     .username("ssar")
        //     .password("1234") //이 상황에서는 password가 ecoding이 안돼서 터짐
        //     .email("ssar@nate.com")
        //     .role(UserEnum.CUSTOMER)
        //         .build();
        User user = newUser("ssar");
        userRepository.save(user);
        // dataInsert();
    }

    //public void teardown (){} //메서드 끝나고 실행되는 것, 하지만 우리는 truncate 사용할 것이므로 필요x

    @Test
    public void login_test() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("ssar");
        loginReqDto.setPassword("1234");
        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("테스트:" + requestBody);

        // when
        ResultActions resultActions = mvc
                .perform(post("/login").content(requestBody)
                        .contentType(APPLICATION_JSON_UTF8));
        String token = resultActions.andReturn().getResponse().getHeader("Authorization");
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트:" + token);
        System.out.println("테스트:" + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer"));
        resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
    }

    public void dataInsert() {
        User user = newUser("ssar");
        userRepository.save(user);
    }
}
