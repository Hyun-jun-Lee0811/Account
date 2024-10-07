package zerobase.account.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zerobase.account.domain.Transaction;
import zerobase.account.type.TransactionResultType;
import zerobase.account.type.TransactionType;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

  private String accountNumber;
  private TransactionType transactionType;
  private TransactionResultType transactionResultType;
  private Long amount;
  private Long balanceSnapShot;
  private String transactionId;
  private LocalDateTime transactedAt;

  public static TransactionDto fromEntity(Transaction transaction) {
    return TransactionDto.builder().
        accountNumber(transaction.getAccount().getAccountNumber())
        .transactionType(transaction.getTransactionType())
        .transactionResultType(transaction.getTransactionResultType())
        .amount(transaction.getAmount())
        .balanceSnapShot(transaction.getBalanceSnapShot())
        .transactionId(transaction.getTransactionId())
        .transactedAt(transaction.getTransactedAt())
        .build();

    // return TransactionDto.fromEntity(transaction);
  }
}
