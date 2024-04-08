package service;

import entity.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class AccountService {

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Transfer error: transfer amount must be greater than zero. Attempt to translate: {}", amount);
            throw new IllegalArgumentException("The transfer amount must be greater than zero.");
        }

        Account firstLock = fromAccount.getId().compareTo(toAccount.getId()) < 0 ? fromAccount : toAccount;
        Account secondLock = fromAccount == firstLock ? toAccount : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (fromAccount.getMoney().compareTo(amount) < 0) {
                    logger.error("Transfer error: insufficient funds in the sender's account {}. Translation attempt: {}, available: {}",
                            fromAccount.getId(), amount, fromAccount.getMoney());
                    throw new IllegalArgumentException("There are not enough funds in the sender's account to complete the transfer.");
                }

                fromAccount.setMoney(fromAccount.getMoney().subtract(amount));
                toAccount.setMoney(toAccount.getMoney().add(amount));

                logger.info("Transfer completed: from {} to {} amount {}. New balance of sender: {}, recipient: {}",
                        fromAccount.getId(), toAccount.getId(), amount, fromAccount.getMoney(), toAccount.getMoney());
            }
        }
    }
}
