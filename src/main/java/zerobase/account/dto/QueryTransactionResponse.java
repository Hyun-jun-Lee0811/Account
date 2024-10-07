package zerobase.account.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zerobase.account.type.TransactionResultType;
import zerobase.account.type.TransactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryTransactionResponse {

  private String accountNumber;
  private TransactionType transactionType;
  private TransactionResultType transactionResult;
  private String transactionId;
  private Long amount;
  private LocalDateTime transactedAt;

  public static QueryTransactionResponse from(TransactionDto transactionDto) {
    return QueryTransactionResponse.builder()
        .accountNumber(transactionDto.getAccountNumber())
        .transactionType(transactionDto.getTransactionType())
        .transactionResult(transactionDto.getTransactionResultType())
        .transactionId(transactionDto.getTransactionId())
        .amount(transactionDto.getAmount())
        .transactedAt(transactionDto.getTransactedAt())
        .build();
  }
}
