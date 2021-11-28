package il.ac.tau.cs.ds.proj1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import il.ac.tau.cs.ds.proj1.AVLTree;

interface IToster {
	public int rope();
}

class Toster implements IToster {
	public int size;
	
	public Toster() {
		size = 8;
	}
	
	@Override
	public int rope() {
		return 7;
	}
}



public class Tester {
	
	public static final long SEED = 4206969;
	
	public static double[] generateNumbers(long seed, int amount, int min, int max) {
		double[] randomList = new double[amount];
		Random generator = new Random(seed);
		for (int i=0;i<amount;i++) {
			randomList[i] = generator.nextInt((max - min) + 1) + min;
		}
		return randomList;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		insertRandomly(tree, 3, 1, SEED);
		tree.printInOrder();
	}
	
	public static void insertRandomly(AVLTree t, int keyMax, int keyMin, long seed) {
		List<Integer> insertions = new ArrayList<Integer>();
		for (int i=keyMin; i<=keyMax; i++) insertions.add(i);
		Collections.shuffle(insertions, new Random(seed));
		
		for (Integer key : insertions) {
			System.out.println("Inserting key " + String.valueOf(key));
			t.insert(key, String.valueOf(key));
		}
	}
	
	@SuppressWarnings("unused")
	public static void OOPTest() {
		System.out.println("Hello");
		Toster oven = new Toster();
		IToster gever = new Toster();
		oven.size = 99;
		IToster bomba = oven;
		Toster totem = (Toster) bomba;
		System.out.println("ddodododododododo " + String.valueOf(bomba.getClass()));
	}
}
