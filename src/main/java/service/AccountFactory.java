package service;

import entity.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountFactory {

    public static List<Account> createAccounts(int sumAccounts) {
        List<Account> accounts = new ArrayList<>();

        for (int i = 0; i < sumAccounts; i++) {
            accounts.add(new Account());
        }

        return accounts;
    }
}
