package test;

import blockchain.*;

public class BlockChainTest {
	public static void main(String[] args) {
		BlockChain chain = new BlockChain(4, 10);
		chain.addListBlock(3);
		chain.printHash();
		System.out.println(chain.verif1());
		chain.jsonWrite("files/jsonfiles/OutputTestO01.txt");
		BlockChain chain2 = new BlockChain("files/jsonfiles/OutputTestO01.txt");
		chain2.jsonWrite("files/jsonfiles/OutputTest0O2.txt");
	}
}
