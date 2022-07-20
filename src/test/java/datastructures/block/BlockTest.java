package datastructures.block;

import datastructures.blockchain.Blockchain;
import datastructures.transaction.Transaction;
import imexpoert.Import;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wallet.Wallet;

import java.time.Instant;

class BlockTest {

    Block block;
    Wallet walletA = new Wallet();
    Wallet walletB = new Wallet();

    Blockchain blockchain = Import.importBlockchainFromPath("savedBlockchains/savedBlockchain.json");


    @Test
    public void BlockCreationWithHashProvidedDoesNotThrow() {
        try{
            block = new Block("ABC-DEF-GHIJK-LM");
        }
        catch(Exception e)
        {
            Assertions.fail();
        }
    }

    @Test
    public void BlockCreationWithNullPreviousBlockHashProvidedDoesNotThrow() {
        try{
            block = new Block(null);
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void BlockGetTimestampWithPreviousBlockHash() {
        long timestamp = Instant.now().getEpochSecond();
        block = new Block("ABC-DEF-GHIJK-LM");
        for (boolean b : new boolean[]{block.getTimeStamp() >= timestamp, block.getTimeStamp() < timestamp + 10}) {
            Assertions.assertTrue(b);
        }
    }

    @Test
    public void BlockGetTimestampWithNullPreviousBlockHash() {
        long timestamp = Instant.now().getEpochSecond();
        block = new Block(null);
        for (boolean b : new boolean[]{block.getTimeStamp() >= timestamp, block.getTimeStamp() < timestamp + 5}) {
            Assertions.assertTrue(b);
        }
    }

    @Test
    public void NewBlockNumberOfTransactionsIsZeroWithPreviousBlockHash() {
        block = new Block("ABC-DEF-GHIJK-LM");
        Assertions.assertNull( block.getTransactions() );
    }

    @Test
    public void NewBlockNumberOfTransactionsIsZeroWithNullBlockHash() {
        block = new Block(null);
        Assertions.assertNull( block.getTransactions() );
    }

    @Test
    public void NewBlockTransactionHashIsNullWithBlockHash() {
        block = new Block("ABC-DEF-GHIJK-LM");
        Assertions.assertNull(block.getMerkleRoot());
    }

    @Test
    public void NewBlockTransactionHashIsNullWithNullBlockHash() {
        block = new Block(null);
        Assertions.assertNull(block.getMerkleRoot());
    }

    @Test
    public void PreviousBlockHashIsNullWithBlockHash() {
        block = new Block("ABC-DEF-GHIJK-LM");
        Assertions.assertEquals("ABC-DEF-GHIJK-LM", block.getPreviousHash());
    }

    @Test
    public void PreviousBlockHashIsNullWithNullBlockHash() {
        block = new Block(null);
        Assertions.assertNull(block.getPreviousHash());
    }

    @Test
    public void NewBlocksTransactionListIsNullWithBlockHash() {
        block = new Block("ABC-DEF-GHIJK-LM");
        Assertions.assertNull(block.getTransactions());
    }

    @Test
    public void NewBlocksTransactionListIsNullWithNullBlockHash() {
        block = new Block(null);
        Assertions.assertNull(block.getTransactions());
    }

    @Test
    public void TransactionIsNotAddedIfBalanceIsBelowValue(){
        block = new Block("ABC-DEF-GHIJK-LM");
        Transaction transaction = walletA.sendFunds(walletB.getPublicKey(),blockchain.getMinimumTransaction(),blockchain);

        Assertions.assertFalse(block.addTransaction(transaction , blockchain));

    }
/*
    @Test
    public void TransactionIsAddedProperlyWithNullPreviousBlockHash(){
        block = new Block(null);
        block.AddTransaction(new RegularTransaction("userA","user",0));
        Transaction[] transactions = block.getTransactions();

        assertTrue(transactions[0].getSender() == "userA");
        assertTrue(transactions[0].getRecipient() == "user");
        assertTrue(transactions[0].getAmount()==0);

        for(int i = 1; i<transactions.length;i++)
        {
            assertTrue(transactions[i] == null);
        }
    }

    @Test
    public void AddingThreeTransactionsResultInCounterIncrement(){
        block = new Block("ABC-DEF-GHIJK-LM");
        block.AddTransaction(new RegularTransaction("userA", "userB",100));
        block.AddTransaction(new RegularTransaction("userB", "userC",50));
        block.AddTransaction(new RegularTransaction("userA", "userD",25));
        assertEquals(3,block.getNumberOfTransactions());
    }
    @Test
    public void AddingOneTransactionsResultInCounterIncrement() {
        block = new Block("ABC-DEF-GHIJK-LM");
        block.AddTransaction(new RegularTransaction("userA", "userB", 100));
        assertEquals(1,block.getNumberOfTransactions());
    }

    @Test
    public void AddingATransactionToFullBlockReturnsFalse()
    {
        block = new Block("ABC-DEF-GHIJK-LM");
        for (int i = 0; i< block.getTransactions().length; i++)
        {
            block.AddTransaction(new RegularTransaction("userA", "userB", 100));
        }
        assertFalse(block.AddTransaction(new RegularTransaction("userA", "userB", 100)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddingNullTransactionThrows()
    {
        block = new Block("ABC-DEF-GHIJK-LM");
        block.AddTransaction(null);
    }

    @Test
    public void IsTransactionsHashValid() {
        block = new Block("ABC-DEF-GHIJK-LM");
        block.AddTransaction(new RegularTransaction("userA", "userB", 100));
        assertEquals(64,block.getTransactionsHash().length());
    }
    */
    @Test
    void calculateHash() {
    }

    @Test
    void mineBlock() {
    }

    @Test
    void addTransaction() {
    }
}