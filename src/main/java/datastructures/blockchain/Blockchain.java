package datastructures.blockchain;


import datastructures.block.Block;
import datastructures.transaction.Transaction;
import datastructures.transaction.TransactionInput;
import datastructures.transaction.TransactionOutput;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import util.StringUtil;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
    private ArrayList<Block> blockchain = new ArrayList<>();
    private HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    private int difficulty = 3;
    private float minimumTransaction = 0.1f;
    private Transaction genesisTransaction;
//Constructor:
    public Blockchain (PublicKey addressOfGenesisTransaction , float initialSupply){
        initializeBlockchain( addressOfGenesisTransaction , initialSupply);
    }
//Constructor for Importing:
    public Blockchain(ArrayList<Block> blockchain, HashMap<String, TransactionOutput> UTXOs){
        this.setBlockchain(blockchain);
        this.setUTXOs(UTXOs);
        this.genesisTransaction = blockchain.get(0).transactions.get(0);
    }

    public void initializeBlockchain(PublicKey addressOfGenesisTransaction , float initialSupply) {
        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());
        //everything to do with the first transaction
        KeyPair firstSignature = StringUtil.generateKeyPair();
        setGenesisTransaction(new Transaction(firstSignature.getPublic(), addressOfGenesisTransaction, initialSupply, null));
        getGenesisTransaction().generateSignature(firstSignature.getPrivate());	 //manually sign the genesis transaction
        getGenesisTransaction().transactionId = "0"; //manually set the transaction id
        getGenesisTransaction().outputs.add(new TransactionOutput(getGenesisTransaction().recipient, getGenesisTransaction().value, getGenesisTransaction().transactionId)); //manually add the Transactions Output
        getUTXOs().put(getGenesisTransaction().outputs.get(0).id, getGenesisTransaction().outputs.get(0)); //it's important to store our first transaction in the UTXOs list.
        //everything to do with the first block
                                                                                                    //System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        if (!genesis.addTransaction(getGenesisTransaction(), this)){
            System.out.println("initializeBlockchain invalid");
        }
        mineAndAddBlock(genesis);
        System.out.println(isChainValid());
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[getDifficulty()]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(getGenesisTransaction().outputs.get(0).id, getGenesisTransaction().outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i = 1; i < getBlockchain().size(); i++) {

            currentBlock = getBlockchain().get(i);
            previousBlock = getBlockchain().get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, getDifficulty()).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop through blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public void mineAndAddBlock(Block newBlock) {
        newBlock.mineBlock(getDifficulty());
        getBlockchain().add(newBlock);
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }
    public void setBlockchain(ArrayList<Block> blockchain) {
        this.blockchain = blockchain;
    }
    public HashMap<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }
    public void setUTXOs(HashMap<String, TransactionOutput> UTXOs) {
        this.UTXOs = UTXOs;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public float getMinimumTransaction() {
        return minimumTransaction;
    }
    public Transaction getGenesisTransaction() {
        return genesisTransaction;
    }
    public void setGenesisTransaction(Transaction genesisTransaction) {
        this.genesisTransaction = genesisTransaction;
    }
}