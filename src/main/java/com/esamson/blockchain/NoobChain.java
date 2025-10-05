package com.esamson.blockchain;

/**
 * Hello world!
 *
 */
public class NoobChain 
{
    public static void main( String[] args )
    {
        var genesisBlock = new Block("Hi im the first block", "0");
        System.out.println("Hash for block 1: " + genesisBlock.hash);

        var secondBlock = new Block("Yo im the second block", genesisBlock.hash);
        System.out.println("Hash for block 2: " + secondBlock.hash);

        var thirdBlock = new Block("Hey im the third block", secondBlock.hash);
        System.out.println("Hash for block 3: " + thirdBlock.hash);
    }
}
