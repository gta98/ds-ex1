package il.ac.tau.cs.ds.proj1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question1 {
	AVLTree[] treeDecreasing, treeRandom;
	
	public int countH(List<Integer> insertions) {
		if (insertions.size() == 0) return 0;
		int h = 0;
		for (int j=0; j<insertions.size()-1; j++) {
			for (int i=j+1; i<insertions.size(); i++) {
				if (insertions.get(i)<insertions.get(j)) {
					h++;
				}
			}
		}
		return h;
	}
	
	public Question1(long seed) {
		// empty constructor
		treeDecreasing = new AVLTree[5+1];
		treeRandom = new AVLTree[5+1];
		for (int i=1; i<=5; i++) {
			treeDecreasing[i] = new AVLTree();
			treeRandom[i] = new AVLTree();
		}
		
		for (int i=1; i<=5; i++) {
			int treeMax = 1000*((int)Math.pow(2, i));
			List<Integer> insertionsDec, insertionsRan;
			insertionsDec = new ArrayList<Integer>();
			for (int k=treeMax-1; k>=0; k--) insertionsDec.add(k);
			insertionsRan = new ArrayList<Integer>();
			for (int k=0; k<insertionsDec.size(); k++) insertionsRan.add(insertionsDec.get(k));
			Collections.shuffle(insertionsRan, new Random(seed));
			
			int costDec = 0;
			int costRan = 0;
			int hRan = countH(insertionsRan);
			int hDec = countH(insertionsDec);
			for (int k=0; k<insertionsDec.size(); k++) {
				costDec += treeDecreasing[i].fingerInsertion(treeMax-insertionsDec.get(k), null);
			}
			for (int k=0; k<insertionsRan.size(); k++) {
				costRan += treeRandom[i].fingerInsertion(insertionsRan.get(k), null);
			}
			
			System.out.println(String.format("Dec: n=%d,h=%d,costDec=%d", treeMax, hDec,costDec));
			System.out.println(String.format("Ran: n=%d,h=%d,costRan=%d", treeMax, hRan,costRan));
		}
	}
	
	public void test() {
		System.out.println("Question1.test() - START");
		
		System.out.println("Question1.test() - END");
	}
	
	public void partA() {
		System.out.println("Question1.partA() - START");
		
		AVLTree t;
		for (int i=1; i<=5; i++) {
			break;
		}
		
		System.out.println("Question1.partA() - END");
	}
	
	public void partB() {
		System.out.println("Question1.partB() - START");
	}
	
	public void partC() {
		System.out.println("Question1.partC() - START");
	}
	
	public void partD() {
		System.out.println("Question1.partD() - START");
	}
	
}
