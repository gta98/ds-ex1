package il.ac.tau.cs.ds.proj1;

import java.util.Random;

import il.ac.tau.cs.ds.proj1.AVLTree.AVLNode;

public class Question2 {
	
	AVLTree[] tree;
	
	public double log2(double x) {
		return Math.log(x)/Math.log(2);
	}
	
	public double calcTheory2(int treeRank) {
		double sum = 0;
		for (int i=1; i<treeRank; i++) {
			sum += log2(i);
		}
		sum /= log2(treeRank);
		return sum;
	}
	
	public Question2(long SEED) {
		Random rand = new Random(SEED);
		int treeSize;
		int splitWith;
		AVLTree[] tree = new AVLTree[10+1];
		for (int i=1; i<=10; i++) {
			tree[i] = new AVLTree();
		}
		
		for (int i=1; i<=2; i++) {
			treeSize = (int)2000*((int)Math.pow(2, i));
			for (int k=0; k<treeSize; k++) {
				tree[i].insert(k, null);
			}
			System.out.println("Is AVL orig? " + String.valueOf(tree[i].isValidAVL()));
			splitWith=rand.nextInt(treeSize)+0;
			int origiRank = tree[i].getRoot().getHeight();
			AVLTree[] trees = tree[i].split(splitWith);
			AVLTree t1 = trees[0];
			AVLTree t2 = trees[1];
			System.out.println("Is AVL 1? " + String.valueOf(t1.isValidAVL()));
			System.out.println("Is AVL 2? " + String.valueOf(t2.isValidAVL()));
			AVLNode node = t1.new AVLNode(splitWith, null);
			int joinCostTotal = t1.join(node, t2);
			System.out.println(String.format("For i=%d\n\tjoinCostMax=%d,joinCostAvg=%f", i, tree[i].joinCostMax,tree[i].joinCostAvg));
			System.out.println(String.format("CostTotal=%d,theory=%f", joinCostTotal, calcTheory2(origiRank)));
		}
	}
	
	public void test() {
		System.out.println("Question2.test() - START");
	}
	
	public void partA() {
		System.out.println("Question2.partA() - START");
	}
	
	public void partB() {
		System.out.println("Question2.partB() - START");
	}
	
	public void partC() {
		System.out.println("Question2.partC() - START");
	}
	
}
