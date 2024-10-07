package zerobase.account.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.account.domain.Account;
import zerobase.account.dto.AccountInfo;
import zerobase.account.dto.CreateAccount;
import zerobase.account.dto.DeleteAccount;
import zerobase.account.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/zerobase/account")
  public CreateAccount.Response createAccount(
      @RequestBody @Valid CreateAccount.Request request) {

    return CreateAccount.Response.from(accountService.createAccount(
        request.getUserId(),
        request.getInitialBalance()));
  }

  @DeleteMapping("/zerobase/account")
  public DeleteAccount.Response deleteAccount(
      @RequestBody @Valid DeleteAccount.Request request) {

    return DeleteAccount.Response.from(accountService.deleteAccount(
        request.getUserId(),
        request.getAccountNumber()));
  }

  @GetMapping("/zerobase/account")
  public List<AccountInfo> getAccountsByUserId(
      @RequestParam("user_id") Long userId
  ) {
    return accountService.getAccountsByUserId(userId)
        .stream().map(accountDto ->
            AccountInfo.builder()
                .accountNumber(accountDto.getAccountNumber())
                .balance(accountDto.getBalance())
                .build())
        .collect(Collectors.toList());
  }

  @GetMapping("/zerobase/account/{id}")
  public Account getAccount(
      @PathVariable Long id) {
    return accountService.getAccount(id);
  }
}
