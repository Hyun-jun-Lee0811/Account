package zerobase.account.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zerobase.account.type.TransactionResultType;
import zerobase.account.type.TransactionType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;
  @Enumerated(EnumType.STRING)
  private TransactionResultType transactionResultType;

  @ManyToOne
  private Account account;
  private Long amount;
  private Long balanceSnapShot;

  private String transactionId;
  private LocalDateTime transactedAt;

}
