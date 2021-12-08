package il.ac.tau.cs.ds.proj1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import il.ac.tau.cs.ds.proj1.AVLTree;

public class Tester {
	
	public static final long SEED = 6617690;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		/// INSERT TESTING HERE ///
		
		System.out.println("Hello World!");
		
		//Question1 q1 = new Question1(SEED); // maybe add randomized parameters here
		//Question2 q2 = new Question2(SEED);
		
		//q1.test();
		//q2.test();
		
		//randomActions2();
		//Question2 q2 = new Question2(SEED);
		//test();
		randomActions();
		
		System.out.println(String.format("Final error count: %d", ErrorReporter.ASSERTION_TRIGGERS));
		assert(ErrorReporter.ASSERTION_TRIGGERS == 0);
		
		/// INSERT TESTING HERE ///
	}

	
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
				if (decision < 3 && insertedNotDeleted.size()>0) {
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
	
	public static void test() {
		System.out.println("test() - START");
		randomActions3();
		randomActions2();
		randomActions();
		insertRandomStress(1000,990);
		System.out.println("test() - END");
	}
	
	public static void randomActions3() {
		System.out.println("randomActions3() - START");
		AVLTree t = new AVLTree();
		for (int i=1; i<=10000000; i++) {
			t.insert(i, "lala"+String.valueOf(i));
		}
		
		int[] keys = t.keysToArray();
		System.out.println(String.format("Key length: %d", keys.length));
		
		System.out.println(String.format("Value: %s", t.search(38457)));
		
		for (int i=1; i<=keys.length; i++) {
			t.delete(i);
		}
		System.out.println("randomActions3() - END");
	}
	
	public static void randomActions2() {
		System.out.println("randomActions2() - START");
		AVLTree t = new AVLTree();
		int max_i = 1000 * ((int)Math.pow(2, 10));
		//insertRandomly(t, max_i, max_i, SEED);
		for (int i=max_i; i>=1; i--) t.insert(i, "lala"+String.valueOf(i));
		if (!t.isValidAVL()) {
			System.out.println("Thats a shame");
			assert(false);
		}
		System.out.println(String.format("Inserted %d", t.keysToArray().length));
		int max_in_left =  ((AVLTree.AVLNode)t.getRoot().getLeft()).getMaxChild().getKey();
		System.out.println(String.format("Max in left %d", max_in_left));
		
		AVLTree[] splitted = t.split(max_in_left);
		assert(splitted[0].isValidAVL());
		assert(splitted[1].isValidAVL());
		
		System.out.println("STOP! hammer time");
		System.out.println("randomActions2() - END");
	}
	
	public static void randomActions() {
		System.out.println("randomActions() - START");
		//insertSpecificList();
		problematicSplitTest1();
		randomSplitTest(SEED,200, 1000);

		randomJoinTrees(SEED,100);
		
		AVLTree t = new AVLTree();
		AVLSequence seq = new AVLSequence(SEED, 6);
		seq.perform(t);

		
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
		System.out.println("randomActions() - END");
	}
	
	public static void randomSplitTest(long seed, int testCount, int maxSize) {
		Random rand = new Random(seed);
		for (int jojo=0; jojo<testCount; jojo++) {
			int keyBoundLower = rand.nextInt(maxSize)+1;
			int keyBoundUpper = keyBoundLower+maxSize + rand.nextInt(maxSize);
			int keySplit = keyBoundLower + rand.nextInt(keyBoundUpper-keyBoundLower);
			AVLTree tree = new AVLTree();
			insertRandomly(tree, keyBoundUpper, keyBoundLower, seed);
			int[] keys = tree.keysToArray();
			//deleteRandomOrder(tree, keys, seed*2);
			assert(tree.isValidAVL());
			
			AVLTree[] splits = tree.split(keySplit);
			for (int i=0; i<splits.length; i++) {
				int[] mKeys = splits[i].keysToArray();
				assert(splits[i].isValidAVL());
			}
			assert(splits[0].size()+splits[1].size()+1 == tree.size());
		}
	}
	
	public static void problematicSplitTest1() {
		AVLTree[] trees = insertRandomStress(17,17);
		AVLTree tree = trees[0];
		int[] keys = tree.keysToArray();
		//deleteRandomOrder(tree, keys, seed*2);
		System.out.println(String.format("We have %d keys", keys.length));
		System.out.println(String.format("Is AVL: %d", tree.isValidAVL()?1:0));
		assert(tree.isValidAVL());
		
		AVLTree[] splits = tree.split(6);
		for (int i=0; i<splits.length; i++) {
			int[] mKeys = splits[i].keysToArray();
			System.out.println(String.format("IN SPLIT #%d we have %d keys", i+1, mKeys.length));
			System.out.println(String.format("Is AVL: %d", splits[i].isValidAVL()?1:0));
			assert(splits[i].isValidAVL());
		}
		assert(splits[0].size()+splits[1].size()+1 == tree.size());
	}
	
	public static void randomJoinTrees(long seed, int testCount) {
		Random rand = new Random(seed);
		int[] keys;
		for (int i=0; i<testCount; i++) {
			int cutoff1 = rand.nextInt(1000)+5;
			int cutoff2 = rand.nextInt(5000)+5;
			cutoff2 += cutoff1 + 10;
			
			int len1 = 1+ cutoff1 - 2;
			int len2 = 1+ cutoff2 - (cutoff1+4);
			AVLTree t1 = new AVLTree();
			AVLTree t2 = new AVLTree();
			insertRandomly(t1, cutoff1, 2, SEED);
			insertRandomly(t2, cutoff2, cutoff1+4, SEED);
			AVLTree.AVLNode x = t1.new AVLNode(cutoff1+2, "separator");
			t1.join(x, t2);
			
			keys = t1.keysToArray();
			//System.out.println(String.format("in joined We have %d keys", keys.length));
			assert(keys.length == t1.debugSize(t1.getRoot()));
			assert(keys.length == len1+len2+1);
			assert(t1.isValidAVL());
		}
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
		System.out.println(String.format("insertRandomStress(%d,%d) - START",limitTop,limitBottom));
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
		
		assert(failures.size()==0);
		System.out.println(String.format("insertRandomStress() - Completed with %d failures (out of %d checked)", failures.size(), checked));
		System.out.println(String.format("insertRandomStress(%d,%d) - END",limitTop,limitBottom));
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
		//System.out.println("Total inserted " + String.valueOf(inserted));
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