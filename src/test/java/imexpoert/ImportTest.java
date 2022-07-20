package imexpoert;

import datastructures.blockchain.Blockchain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wallet.Wallet;

class ImportTest {
    Wallet walletA = new Wallet();
    Wallet walletB;

    Blockchain blockchainA = new Blockchain(walletA.getPublicKey(),100);
    Blockchain blockchainB;

    @Test
    public void isWalletCorrectlyImported(){
        Export.exportWallet(walletA, "walletA");
        walletB = Import.importWalletFromPath("savedWallets/" + "walletA" + ".json");
        Assertions.assertEquals(walletA.hashCode(),walletB.hashCode());
    }

    @Test
    public void isBlockchainCorrectlyImported(){
        Export.exportBlockchain(blockchainA);
        blockchainB = Import.importBlockchainFromPath("savedBlockchains/" + "savedBlockchain" + ".json");
        Assertions.assertEquals(blockchainA.hashCode(),blockchainB.hashCode());
    }

}