package service;

import entity.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class StartTransactions {

    private static final Logger logger = LogManager.getLogger(StartTransactions.class);
    private static final int SUM_OF_PROCESSES = 4;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(SUM_OF_PROCESSES);
    private final AccountService accountService;
    private final List<Account> accounts;
    private final AtomicInteger transactionCount = new AtomicInteger(0);
    private final int maxTransactions;
    private final BigDecimal maxTransferAmount;

    public StartTransactions(int maxTransactions, int maxTransferAmount, int sumOfAccounts) {
        this.accountService = new AccountService();
        this.accounts = AccountFactory.createAccounts(sumOfAccounts);
        this.maxTransactions = maxTransactions;
        this.maxTransferAmount = BigDecimal.valueOf(maxTransferAmount);
    }

    public void performRandomTransactions() {
        BigDecimal initialTotalBalance = logTotalBalance("Initial total balances of all accounts: ");

        for (int i = 0; i < SUM_OF_PROCESSES; i++) {
            executorService.submit(() -> {
                while (transactionCount.getAndIncrement() < maxTransactions) {
                    try {
                        Account fromAccount = accounts.get(ThreadLocalRandom.current().nextInt(accounts.size()));
                        Account toAccount;
                        do {
                            toAccount = accounts.get(ThreadLocalRandom.current().nextInt(accounts.size()));
                        } while (toAccount.getId().equals(fromAccount.getId()));

                        BigDecimal amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()).multiply(maxTransferAmount);
                        accountService.transfer(fromAccount, toAccount, amount);

                        Thread.sleep(ThreadLocalRandom.current().nextInt(1001, 2000));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.error("Thread was interrupted", e);
                    } catch (Exception e) {
                        logger.error("An error occurred during the transaction", e);
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) { // Shorter wait time
                logger.info("Executor did not terminate in the specified time, forcing shutdown now...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Executor termination was interrupted", e);
        }

        BigDecimal finalTotalBalance = logTotalBalance("Final total balances of all accounts: ");
        if (initialTotalBalance.compareTo(finalTotalBalance) != 0) {
            logger.warn("The initial and final total balances do not match.");
        }
    }

    private BigDecimal logTotalBalance(String message) {
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info(message + totalBalance);
        return totalBalance;
    }
}
