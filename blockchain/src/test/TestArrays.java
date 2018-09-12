package test;

import java.util.ArrayList;

public class TestArrays {
	public static void main(String[] args) {
		ArrayList<Integer> test = new ArrayList<Integer>();
		for(int i = 0 ; i < 6 ; i++) {
			Integer inc = i;
			test.add(inc);
		}
		System.out.println(test);
		System.out.println(test.size());
	}
}
