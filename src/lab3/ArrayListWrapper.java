package lab3;

import java.util.ArrayList;

public class ArrayListWrapper {
	
	public ArrayList<Double> list;
	public long seed;
	
	public ArrayListWrapper(ArrayList<Double> list, long seed) {
		list = new ArrayList<>();
		this.seed = seed;
		System.out.println("Seed " + this.seed + " created");
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("Seed " + this.seed + " collected");
	}
}
