package lab3;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		
		int cacheSize = 50;
		Random generator = new Random(5);
		Map<Long, SoftReference<ArrayListWrapper>> cache = new ConcurrentHashMap<>();
		
		List<Long> seeds = new ArrayList<Long>() {{
			add((long) 10);
			add((long) 11);
			add((long) 12);
			add((long) 13);
			add((long) 14);
		}};
		
		for(int i = 0; i < 50; i ++) {
			long seed = seeds.get(generator.nextInt()& Integer.MAX_VALUE % seeds.size());
			cache.put(seed, getList(seed));
		}
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		for(int i = 0; i < 13; i++) {
			executor.submit(() -> {
				String threadName = Thread.currentThread().getName();
				long seed = seeds.get(generator.nextInt()& Integer.MAX_VALUE % seeds.size());
				
				SoftReference<ArrayListWrapper> list = cache.getOrDefault(seed, getList(seed));
				
				double average = 0;
				System.out.println(list.get().list.size());
				for(double val : list.get().list) {
					average += val;
					System.out.println("dbg");
				}
				average /= list.get().list.size();
				System.out.println(threadName + " calculated average " + average + "for seed " + seed);
			});			
		}
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
		    System.out.println("attempt to shutdown executor");
		    
		    executor.shutdown();
		    executor.awaitTermination(60, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		}
		finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executor.shutdownNow();
		    System.out.println("shutdown finished");
		}
	}

	public static SoftReference<ArrayListWrapper> getList(long seed) {
		ArrayList<Double> list = new ArrayList<>();
		
		
		
		Random generator = new Random(seed);
		int listSize = (generator.nextInt() & Integer.MAX_VALUE) % 1000000;
		for(int i = 0; i < listSize; i++) {
			list.add(generator.nextDouble());
		}
		SoftReference<ArrayListWrapper> out = new SoftReference<ArrayListWrapper>(new ArrayListWrapper(list, seed));
		return out;
	}
}
