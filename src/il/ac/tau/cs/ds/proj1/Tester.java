package il.ac.tau.cs.ds.proj1;

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
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("Hello");
		Toster oven = new Toster();
		IToster gever = new Toster();
		oven.size = 99;
		IToster bomba = oven;
		Toster totem = (Toster) bomba;
		System.out.println("ddodododododododo " + String.valueOf(bomba.getClass()));
	}
}
