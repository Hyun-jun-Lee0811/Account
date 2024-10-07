package zerobase.account.service;

import static zerobase.account.type.AccountStatus.IN_USE;
import static zerobase.account.type.ErrorCode.ACCOUNT_ALREADY_UNREGISTERED;
import static zerobase.account.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static zerobase.account.type.ErrorCode.BALANCE_NOT_EMPTY;
import static zerobase.account.type.ErrorCode.MAX_ACCOUNT_PER_USER_10;
import static zerobase.account.type.ErrorCode.USER_ACCOUNT_UN_MATCH;
import static zerobase.account.type.ErrorCode.USER_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.account.domain.Account;
import zerobase.account.domain.AccountUser;
import zerobase.account.dto.AccountDto;
import zerobase.account.exception.AccountException;
import zerobase.account.repository.AccountRepository;
import zerobase.account.repository.AccountUserRepository;
import zerobase.account.type.AccountStatus;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountUserRepository accountUserRepository;

  @Transactional
  public AccountDto createAccount(Long userId, Long initialBalance) {
    AccountUser accountUser = getAccountUser(userId);

    validateCreateAccount(accountUser);

    String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
        .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
        .orElse("1000000000");

    return AccountDto.fromEntity(
        accountRepository.save(
            Account.builder()
                .accountUser(accountUser)
                .accountStatus(IN_USE)
                .accountNumber(newAccountNumber)
                .balance(initialBalance)
                .registeredAt(LocalDateTime.now())
                .build()
        ));
  }

  private void validateCreateAccount(AccountUser accountUser) {
    if (accountRepository.countByAccountUser(accountUser) >= 10) {
      throw new AccountException(MAX_ACCOUNT_PER_USER_10);
    }
  }

  @Transactional
  public Account getAccount(Long id) {
    if (id < 0) {
      throw new RuntimeException("Minus");
    }
    return accountRepository.findById(id).get();
  }

  @Transactional
  public AccountDto deleteAccount(Long userId, String accountNumber) {
    AccountUser accountUser = getAccountUser(userId);

    Account account = accountRepository.findByAccountNumber(accountNumber).
        orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));

    validateDeleteAccount(accountUser, account);

    account.setAccountStatus(AccountStatus.UNREGISTERED);
    account.setUnRegisteredAt(LocalDateTime.now());

    accountRepository.save(account);

    return AccountDto.fromEntity(account);
  }

  private void validateDeleteAccount(AccountUser accountUser, Account account) {
    if (!Objects.equals(accountUser.getId(), account.getAccountUser().getId())) {
      throw new AccountException(USER_ACCOUNT_UN_MATCH);
    }
    if (account.getAccountStatus() == AccountStatus.UNREGISTERED) {
      throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
    }
    if (account.getBalance() > 0) {
      throw new AccountException(BALANCE_NOT_EMPTY);
    }
  }

  @Transactional
  public List<AccountDto> getAccountsByUserId(Long userId) {
    AccountUser accountUser = getAccountUser(userId);

    List<Account> accounts = accountRepository.findByAccountUser(accountUser);

    return accounts.stream().map(AccountDto::fromEntity)
        .collect(Collectors.toList());
  }

  private AccountUser getAccountUser(Long userId) {
    return accountUserRepository.
        findById(userId).orElseThrow(()
            -> new AccountException(USER_NOT_FOUND));
  }
}
