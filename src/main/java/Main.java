import service.StartTransactions;

public class Main {
    public static void main(String[] args) {
        StartTransactions startTransactions =
                new StartTransactions(30, 5000, 4);

        startTransactions.performRandomTransactions();

    }
}
