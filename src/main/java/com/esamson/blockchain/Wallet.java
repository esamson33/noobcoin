package com.esamson.blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            var keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            var random = SecureRandom.getInstance("SHA1PRNG");
            var ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec, random);
            var keyPair = keyGen.genKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
        catch ( Exception e ) {
            throw new RuntimeException(e);
        }
    }

    /// Returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for (var item : NoobChain.UTXOs.entrySet()) {
            var UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    ///Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient, float value) {
        if (getBalance() < value) {
            System.out.println("#Not enough funds to send transaction. Transaction discarded");
            return null;
        }

        var inputs = new ArrayList<TransactionInput>();
        float total = 0;

        for (var item : UTXOs.entrySet()) {
            var UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) break;
        }

        var newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (var input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}