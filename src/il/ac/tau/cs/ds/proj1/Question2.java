package il.ac.tau.cs.ds.proj1;

import java.util.Random;

public class Question2 {
	
	AVLTree[] tree;
	
	public Question2(long SEED) {
		Random rand = new Random(SEED);
		int treeSize;
		AVLTree[] tree = new AVLTree[10+1];
		for (int i=1; i<=10; i++) {
			tree[i] = new AVLTree();
		}
		
		for (int i=1; i<=2; i++) {
			treeSize = (int)2000*((int)Math.pow(2, i));
			for (int k=0; k<treeSize; k++) {
				tree[i].insert(k, null);
			}
			tree[i].split(rand.nextInt(treeSize)+0);
			System.out.println(String.format("For i=%d\n\tjoinCostMax=%d,joinCostAvg=%f", i, tree[i].joinCostMax,tree[i].joinCostAvg));
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
