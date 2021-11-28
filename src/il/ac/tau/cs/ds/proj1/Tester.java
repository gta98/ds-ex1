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
	public IToster oven;
	
	public Toster() {
		size = 8;
	}
	
	@Override
	public int rope() {
		return 7;
	}
	
	public IToster getOven() { return oven; }
	public void setOven(IToster mOven) { this.oven = mOven; }
}



public class Tester {
	
	public static final long SEED = 4206969;
	
	enum AVLTaskType {
		INSERT,
		DELETE
	}
	
	static class AVLTask {
		AVLTaskType task;
		int key;
		
		public AVLTask(AVLTaskType task, int key) {
			this.task = task;
			this.key = key;
		}
		
		@Override
		public String toString() {
			return "task="+this.task.toString()+", key="+this.key;
		}
		
		public AVLTask() {}
	}
	
	static class AVLSequence {
		List<AVLTask> tasks;
		Random rand;
		int factor;
		long seed;
		int count;
		
		public AVLSequence(long seed, int count) {
			this.factor = 1;
			this.seed = seed;
			this.count = count;
			this.Randomize();
		}
		
		public void Randomize() {
			rand = new Random(seed*factor);
			tasks = new LinkedList<AVLTask>();
			
			List<Integer> insertions = new ArrayList<Integer>();
			for (int i=1; i<=count; i++) insertions.add(i);
			Collections.shuffle(insertions, new Random(seed*factor));
			
			List<Integer> deleted = new ArrayList<Integer>();
			List<Integer> inserted = new ArrayList<Integer>();
			List<Integer> insertedNotDeleted = new ArrayList<Integer>();
			
			int decision;
			int deleteKey;
			int keyIdx = 0;
			while (keyIdx < insertions.size()) {
				decision = rand.nextInt(10);
				if (decision < 3 && inserted.size()-deleted.size()>0) {
					deleteKey = rand.nextInt(insertedNotDeleted.size());
					tasks.add(new AVLTask(AVLTaskType.DELETE, insertedNotDeleted.get(deleteKey)));
					insertedNotDeleted.remove(deleteKey);
				} else {
					tasks.add(new AVLTask(AVLTaskType.INSERT, insertions.get(keyIdx)));
					inserted.add(keyIdx);
					insertedNotDeleted.add(keyIdx);
					keyIdx++;
				}
			}
			factor++;
		}
		
		public void perform(AVLTree t) {
			int success = 0;
			int failure = 0;
			for (AVLTask task : tasks) {
				switch (task.task) {
					case INSERT: {
						t.insert(task.key, null);
						break;
					}
					case DELETE: {
						t.delete(task.key);
						break;
					}
					default: {
						break;
					}
				}
				if (!t.isValidAVL()) {
					System.out.println(String.format("Task %s caused invalid AVL", task));
					failure++;
				} else {
					success++;
				}
			}
			System.out.println("For sequence: ");
			System.out.println(String.format("success=%d, failure=%d", success, failure));
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//insertSpecificList();
		AVLTree[] trees = insertRandomStress(6,6);
		AVLTree tree = trees[0];
		int[] keys = tree.keysToArray();
		deleteRandomOrder(tree, keys, SEED*2);
		/*AVLTree t = new AVLTree();
		AVLSequence seq = new AVLSequence(SEED, 6);
		seq.perform(t);*/

		
		/*tree.delete(3);
		System.out.println(String.format("Is AVL: %d", tree.isValidAVL()?1:0));
		tree.printInOrder();
		tree.delete(1);
		System.out.println(String.format("Is AVL: %d", (tree.isValidAVL()?1:0)));
		tree.printInOrder();
		tree.delete(5);
		tree.printInOrder();
		System.out.println(String.format("Is AVL: %d", (tree.isValidAVL()?1:0)));*/
		//OOPTest();
	}
	
	
	public static void deleteRandomOrder(AVLTree t, int[] keys, long seed) {
		List<Integer> deletions = new ArrayList<Integer>();
		for (int i=0; i<keys.length; i++) deletions.add(keys[i]);
		Collections.shuffle(deletions, new Random(seed));
		int deleted = 0;
		for (Integer key : deletions) {
			t.delete(key);
			if (!t.isValidAVL()) {
				System.out.println("Failure with list: " + deletions.toString());
				System.out.println("key=" + key + ", deleted=" + String.valueOf(deleted));
				assert(false);
			}
			deleted++;
		}
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
	
	public static void playWithTree(AVLTree t, int keyMax, int keyMin, long seed) {
		
	}
	
	public static AVLTree[] insertRandomStress(int limitTop, int limitBottom) {
		int cyclePrintMax = (limitTop-limitBottom)/20;
		int cyclePrintIdx = 0;
		AVLTree[] trees = new AVLTree[limitTop-limitBottom+1];
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
			trees[i-limitBottom] = tree;
		}
		System.out.println(String.format("insertRandomStress() - Completed with %d failures (out of %d checked)", failures.size(), checked));
		return trees;
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
		Toster bobo = new Toster();
		oven.setOven(bobo);
		if (oven.getOven() == bobo) {
			System.out.println("Ident");
		} else {
			System.out.println("BAD!!");
		}
		IToster gever = new Toster();
		
		oven.size = 99;
		IToster bomba = oven;
		Toster totem = (Toster) bomba;
		System.out.println("ddodododododododo " + String.valueOf(bomba.getClass()));
	}
}
