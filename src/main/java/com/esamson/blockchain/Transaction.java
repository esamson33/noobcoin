package com.esamson.blockchain;

import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;        // hash of the transaction
    public PublicKey sender;            // address of the sender
    public PublicKey recipient;         // address of the recipient
    public float value;                 // transaction amount
    public byte[] signature;            // verify using sender public key

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(
            StringUtil.getStringFromKey(sender) +
            StringUtil.getStringFromKey(recipient) +
            Float.toString(value) + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + 
            StringUtil.getStringFromKey(recipient) + 
            Float.toString(value);
        signature = StringUtil.applyEcdsaSig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) +
            StringUtil.getStringFromKey(recipient) +
            Float.toString(value);
        return StringUtil.verifyEcdsaSig(sender, data, signature);
    }

    public boolean processTransaction() {
        if (verifySignature() == false) {
            System.out.println("#Transaction signature failed to verify");
            return false;
        }

        for (TransactionInput i : inputs) {
            i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
        }

        if (getInputsValue() < NoobChain.minimumTransaction) {
            System.out.println("#Transaction inputs to small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        for (TransactionOutput o : outputs) {
            NoobChain.UTXOs.put(o.id, o);
        }

        for (TransactionInput i : inputs) {
            if (i.UTXO == null) continue;
            NoobChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }
}
