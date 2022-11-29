package shop.mtcoding.bank.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountSaveRespDto;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountSaveRespDto 계좌생성(AccountSaveReqDto accountSaveReqDto, Long userId) {
        // Account account = accountSaveReqDto.toEntity(user);
        // Account accountPS = accountRepository.save(account);
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("탈퇴한 유저로 계좌를 생성할 수 없습니다.", HttpStatus.FORBIDDEN));
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
        log.debug("디버그 : 계좌생성 서비스 호출됨");
        // // 1. 검증(권한, 값 검증)
        // User userPS = userRepository.findById(userId)
        //         .orElseThrow(
        //                 () -> new CustomApiException("탈퇴된 유저로 계좌를 생성할 수 없습니다.", HttpStatus.FORBIDDEN));

        // // 2. 실행
        // Account account = accountSaveReqDto.toEntity(userPS);
        // Account accountPS = accountRepository.save(account);

        // 3. DTO 응답
        return new AccountSaveRespDto(accountPS);
    }

}
