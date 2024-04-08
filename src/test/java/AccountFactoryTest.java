import static org.junit.Assert.*;

import entity.Account;
import org.junit.Test;
import service.AccountFactory;

import java.util.List;

public class AccountFactoryTest {

    @Test
    public void testAccountsCreation() {
        int numberOfAccounts = 4;
        List<Account> accounts = AccountFactory.createAccounts(numberOfAccounts);
        assertEquals(numberOfAccounts, accounts.size());
        long uniqueIds = accounts.stream().map(Account::getId).distinct().count();
        assertEquals(numberOfAccounts, uniqueIds);
    }
}

