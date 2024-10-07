package zerobase.account.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobase.account.aop.AccountLock;
import zerobase.account.dto.CancelBalance;
import zerobase.account.dto.QueryTransactionResponse;
import zerobase.account.dto.UseBalance;
import zerobase.account.service.TransactionService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping("/transaction/use")
  @AccountLock
  public UseBalance.Response unBalance(
      @Valid @RequestBody UseBalance.Request request
  ) throws InterruptedException {
    try {
      Thread.sleep(3000L);
      return UseBalance.Response.from(transactionService.useBalance(request.getUserId(),
          request.getAccountNumber(), request.getAmount())
      );
    } catch (ArithmeticException e) {
      log.error("Failed to use balance");

      transactionService.saveFailedUseTransaction(
          request.getAccountNumber(),
          request.getAmount()
      );

      throw e;
    }
  }

  @PostMapping("/transaction/cancel")
  @AccountLock
  public CancelBalance.Response cancelBalance(
      @Valid @RequestBody CancelBalance.Request request
  ) {
    try {
      return CancelBalance.Response.from(
          transactionService.cancelBalance(request.getTransactionId(),
              request.getAccountNumber(), request.getAmount())
      );
    } catch (ArithmeticException e) {
      log.error("Failed to use balance");

      transactionService.saveFailedCancelTransaction(
          request.getAccountNumber(),
          request.getAmount()
      );

      throw e;
    }
  }

  @GetMapping("/transaction/{transactionId}")
  public QueryTransactionResponse queryTransaction(
      @PathVariable String transactionId) {
    return QueryTransactionResponse.from(
        transactionService.queryTransaction(transactionId));
  }
}
