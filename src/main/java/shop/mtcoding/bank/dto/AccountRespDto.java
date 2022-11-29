package shop.mtcoding.bank.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.util.CustomDateUtil;

public class AccountRespDto {
    @Setter
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    @Setter
    @Getter
    public static class AccountDeleteRespDto {
        private Long accountNumber;
        private Boolean isUse;
        private String deleteDate;

        public AccountDeleteRespDto(Account account) {
            this.accountNumber = account.getNumber();
            this.isUse = account.getIsActive();
            // 더티체킹 타이밍 보다 빨라서 DB값 못씀.
            this.deleteDate = CustomDateUtil.toStringFormat(LocalDateTime.now());
        }

    }
}
