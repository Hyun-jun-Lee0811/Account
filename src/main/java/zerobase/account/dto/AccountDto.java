package zerobase.account.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zerobase.account.domain.Account;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

  private Long useId;
  private String accountNumber;
  private Long balance;

  private LocalDateTime registeredAt;
  private LocalDateTime unRegisteredAt;

  public static AccountDto fromEntity(Account account) {
    return AccountDto.builder()
        .useId(account.getAccountUser().getId()).
        accountNumber(account.getAccountNumber()).
        balance(account.getBalance()).
        registeredAt(account.getRegisteredAt()).
        unRegisteredAt(account.getUnRegisteredAt()).
        build();
  }
}
