package il.ac.tau.cs.ds.proj1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
		//insertSpecificList();
		insertRandomStress(10000, 9999);
	}
	
	public static void insertSpecificList() {
		AVLTree t = new AVLTree();
		List<Integer> insertions = new ArrayList<Integer>();
		t.insert(5, "5");
		t.insert(6, "6");
		t.insert(2, "2");
		t.insert(1, "1");
		t.insert(3, "3");
		t.insert(4, "4");
		assert(t.isValidAVL());
	}
	
	public static void insertRandomStress(int limitTop, int limitBottom) {
		int cyclePrintMax = (limitTop-limitBottom)/20;
		int cyclePrintIdx = 0;
		
		List<Integer> failures = new LinkedList<Integer>();
		int checked = 0;
		for (int i=limitBottom; i<=limitTop; i++) {
			cyclePrintIdx++;
			if (cyclePrintIdx == cyclePrintMax) {
				cyclePrintIdx = 0;
				System.out.println("Checked " + String.valueOf(i));
			}
			//if (i!=9 && i!=4) continue;
			checked++;
			AVLTree tree = new AVLTree();
			insertRandomly(tree, i, 1, SEED);
			//tree.printInOrder();
			assert(tree.isValidAVL());
			assert(tree.size()==i);
			if (!tree.isValidAVL()) {
				System.out.println("Error for i="+String.valueOf(i));
				failures.add(i);
			}
		}
		System.out.println(String.format("Completed with %d failures (out of %d checked)", failures.size(), checked));
	}
	
	public static void insertRandomly(AVLTree t, int keyMax, int keyMin, long seed) {
		List<Integer> insertions = new ArrayList<Integer>();
		for (int i=keyMin; i<=keyMax; i++) insertions.add(i);
		Collections.shuffle(insertions, new Random(seed));
		insertList(t, insertions);
	}
	
	public static void insertList(AVLTree t, List<Integer> insertions) {
		//System.out.println("Insertion order: " + insertions.toString());
		int inserted=0;
		for (Integer key : insertions) {
			//System.out.println("=== Inserting key " + String.valueOf(key));
			t.insert(key, String.valueOf(key));
			if (!t.isValidAVL()) {
				System.out.println("Failure with list: " + insertions.toString());
				System.out.println("key=" + key + ", inserted=" + String.valueOf(inserted));
				assert(false);
			}
			inserted++;
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
