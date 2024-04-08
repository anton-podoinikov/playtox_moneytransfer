import static org.junit.Assert.*;

import entity.Account;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;

import java.math.BigDecimal;

public class AccountServiceTest {

    private AccountService accountService;
    private Account fromAccount;
    private Account toAccount;

    @Before
    public void setUp() {
        accountService = new AccountService();
        fromAccount = new Account();
        toAccount = new Account();
        fromAccount.setMoney(BigDecimal.valueOf(1000));
        toAccount.setMoney(BigDecimal.valueOf(1000));
    }

    @Test
    public void testSuccessfulTransfer() {
        BigDecimal transferAmount = BigDecimal.valueOf(500);
        accountService.transfer(fromAccount, toAccount, transferAmount);
        assertEquals(0, fromAccount.getMoney().compareTo(BigDecimal.valueOf(500)));
        assertEquals(0, toAccount.getMoney().compareTo(BigDecimal.valueOf(1500)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferWithInsufficientFunds() {
        BigDecimal transferAmount = BigDecimal.valueOf(1500);
        accountService.transfer(fromAccount, toAccount, transferAmount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferWithNegativeAmount() {
        BigDecimal transferAmount = BigDecimal.valueOf(-100);
        accountService.transfer(fromAccount, toAccount, transferAmount);
    }
}
