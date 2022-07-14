package datastructures;

public class Transaction {
    private String sender;
    private String receiver;
    private int amount;

    public Transaction(String sender, String receiver, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public int getAmount() {
        return amount;
    }
}
