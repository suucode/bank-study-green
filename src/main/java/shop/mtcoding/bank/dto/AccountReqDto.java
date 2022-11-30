package shop.mtcoding.bank.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;

public class AccountReqDto {
    @Setter
    @Getter
    public static class AccountSaveReqDto {

        @Digits(integer = 4, fraction = 4)
        @NotNull(message = "계좌번호는 필수입니다")
        private Long number;

        @NotBlank(message = "계좌비밀번호는 필수입니다")
        @Pattern(regexp = "[1-9]{4,4}", message = "비밀번호는 숫자 4자리로 입력해주세요.")
        private String password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number.longValue())
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .isActive(true)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class AccountDeleteReqDto {
        private String password; //하나만 받더라도 Dto를 만들어야한다..

        //private Long userId; //서비스 로직
    }
}
