package shop.mtcoding.bank.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.domain.user.User;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthorizationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void authorization_success_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build(); //newUser로는 id를 넣을 수 없어서 만들 수 없다
        LoginUser loginUser = new LoginUser(user);
        String jwtHeaderKey = JwtProperties.HEADER_KEY;
        String jwtToken = JwtProcess.create(loginUser);

        System.out.println("테스트 : " + jwtToken);

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/user/test").header(jwtHeaderKey, jwtToken));

        // then
        resultActions.andExpect(status().isNotFound()); //404가 나오면 잘된것
    }

    @Test
    public void authorization_fail_test() throws Exception {
        //given //given이 없으므로 header없이 실행하면 됨
        // when
        ResultActions resultActions = mvc
                .perform(get("/api/user/test"));

        // then
        resultActions.andExpect(status().isForbidden()); //403이 나오면 잘된것
    }
}
