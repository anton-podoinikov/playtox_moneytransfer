package entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private static final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(10000);
    private final String id;
    private BigDecimal money;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.money = STARTING_BALANCE;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
