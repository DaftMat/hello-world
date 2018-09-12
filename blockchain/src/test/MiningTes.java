package test;

import blockchain.Block;

public class MiningTes {
	public static void main(String[] args) {
		Block bloc = new Block(0, 25, "a3989cd158d4c3b6f91b8372172063b4de9f99267ea09063fbbf260539b45b0f");
		bloc.listTransRandom();
		bloc.mine(10);
		System.out.println(bloc.getDate());
	}
}
