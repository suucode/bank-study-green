package shop.mtcoding.bank.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.util.CustomDateUtil;

public class AccountRespDto {

    @Setter
    @Getter
    public static class AccountListRespDto {
        private UserDto user;
        private List<AccountDto> accounts;

        public AccountListRespDto(List<Account> accounts) {
            this.user = new UserDto(accounts.get(0).getUser());
            this.accounts = accounts.stream().map((account)->new AccountDto(account)).collect(Collectors.toList()); //streamApi
        }

        @Setter
        @Getter
        public class UserDto {
            private Long id;
            private String fullName;

            public UserDto(User user) {
                this.id = user.getId();
                this.fullName = user.getFullName();
            }
        }

        @Setter
        @Getter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

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
