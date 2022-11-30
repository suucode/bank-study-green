package shop.mtcoding.bank.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.AccountRespDto;
import shop.mtcoding.bank.dto.AccountReqDto.AccountDeleteReqDto;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountSaveRespDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    // 계좌상세보기 (Account + List<Transaction>) Transaction 구현하고 만들기

    // 본인계좌삭제하기
    @Transactional
    public void 본인_계좌삭제(AccountDeleteReqDto accountDeleteReqDto, Long userId, Long accountId) { //여기서 userId를 같이받던가 Dto에 서비스로직으로 Id를 넣어주던가
        // 계좌 확인(존재여부)
        Account account = accountRepository.findById(accountId).orElseThrow(()->new CustomApiException("해당 계좌가 없습니다.", HttpStatus.BAD_REQUEST));

        // // 계좌 소유자 확인(로그인한 사람이 계좌 소유자인지)
        // account.isOwner(userId);

        // //계좌 비밀번호 확인(비밀번호가 맞는지)
        // account.checkPassword(accountDeleteReqDto.getPassword());

        //계좌 삭제하기
        account.deleteAccount(userId, accountDeleteReqDto.getPassword());
        
        // 더티체킹 
    }

    // 본인계좌목록보기
    public AccountListRespDto 본인_계좌목록보기(Long userId) {
        List<Account> accountListPS = accountRepository.findByActiveUserId(userId);
        if (accountListPS.size() == 0) {
            User userPS = userRepository.findById(userId).orElseThrow(()->new CustomApiException("사용자를 찾을 수 없습니다", HttpStatus.BAD_REQUEST));
            return new AccountListRespDto(accountListPS);
        } else {
            return new AccountListRespDto(accountListPS);
        }
    }

    //select 두번
    public AccountListRespDto 본인_계좌목록보기v2(Long userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저못찾음", HttpStatus.BAD_REQUEST));
        List<Account> accountListPS = accountRepository.findByActiveUserIdv2(userId);
        return new AccountListRespDto(userPS, accountListPS);
    }
    
    //양방향 매핑
    public void 본인_계좌목록보기v3(Long userId) {


    }

    @Transactional
    public AccountSaveRespDto 계좌생성(AccountSaveReqDto accountSaveReqDto, Long userId) {
        // Account account = accountSaveReqDto.toEntity(user);
        // Account accountPS = accountRepository.save(account);
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("탈퇴한 유저로 계좌를 생성할 수 없습니다.", HttpStatus.FORBIDDEN));
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
        //log.debug("디버그 : 계좌생성 서비스 호출됨");
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
