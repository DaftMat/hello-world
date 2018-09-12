package test;

import blockchain.Block;

public class MerkleTest {
	public static void main(String[] args) {
		Block bloc = new Block(0, 25, "ta mere");
		
		bloc.listTransRandom();
		bloc.merkleRoot();
		//System.out.println(bloc.getMerkleRoot());
	}
}
