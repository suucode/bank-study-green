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

        @NotBlank(message = "계좌주 이름은 필수입니다")
        @Pattern(regexp = "[가-힣]{3,10}", message = "계좌주 이름은 한글, 길이는 최소4, 최대10 입니다")
        private String ownerName;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number.longValue())
                    .password(password)
                    .ownerName(ownerName)
                    .balance(1000L)
                    .user(user)
                    .isActive(true)
                    .build();
        }
    }
}
