package imexpoert;

import datastructures.blockchain.Blockchain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wallet.Wallet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ExportTest {


    Wallet walletA = new Wallet();
    Blockchain blockchain = new Blockchain(walletA.getPublicKey(), 100);
    @Test
    public void isWalletExported() {
        Export.exportWallet(walletA, "walletA");
        Path path = Paths.get("savedWallets/" +
                "walletA" +
                ".json");
        boolean exists = Files.exists(path);
        Assertions.assertTrue(exists);
    }
    @Test
    public void isBlockchainExported(){
        Export.exportBlockchain(blockchain);
        Path path = Paths.get("savedBlockchains/" +
                "savedBlockchain" +
                ".json");
        boolean exists = Files.exists(path);
        Assertions.assertTrue(exists);
    }

    @Test
    void main() {
    }

    @Test
    void exportToPath() {

    }
}