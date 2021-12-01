package il.ac.tau.cs.ds.proj1;

public class Question1 {
	public Question1() {
		// empty constructor
	}
	
	public void test() {
		System.out.println("Question1.test() - START");
		
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
