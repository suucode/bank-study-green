package shop.mtcoding.bank.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.dto.AccountReqDto.AccountDeleteReqDto;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.service.AccountService;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AccountApiController {
    private final AccountService accountService;

    @PutMapping("/account/{accountId}/delete")
    public ResponseEntity<?> delete(@PathVariable Long accountId, @RequestBody AccountDeleteReqDto accountDeleteReqDto,
            @AuthenticationPrincipal LoginUser loginUser) {
        accountService.본인_계좌삭제(accountDeleteReqDto, loginUser.getUser().getId(), accountId);
        return new ResponseEntity<>(new ResponseDto<>("삭제완료", null), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/account")
    public ResponseEntity<?> list(@PathVariable Long userId, @AuthenticationPrincipal LoginUser loginUser) {
        if (userId != loginUser.getUser().getId()) {
            throw new CustomApiException("권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        AccountListRespDto accountListRespDto = accountService.본인_계좌목록보기(userId);
        return new ResponseEntity<>(new ResponseDto<>("계좌목록보기 성공", accountListRespDto), HttpStatus.OK);
    }

    @PostMapping("/account")
    public ResponseEntity<?> save(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto, //바로 밑에 만들어뒀다가 dto로 빼내기
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) { //security context에 있는 user detail을 가져옴

        AccountSaveRespDto accountSaveRespDto = accountService.계좌생성(accountSaveReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>("계좌생성완료", accountSaveRespDto),
                HttpStatus.CREATED);
    }
}
