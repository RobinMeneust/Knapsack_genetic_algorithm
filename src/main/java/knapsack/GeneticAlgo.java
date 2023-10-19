package knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.Random;

public class GeneticAlgo {
	private float maxWeight;
	private HashMap<Item,Integer> items;
	private Random rand = new Random();

	public GeneticAlgo(float maxWeigth, HashMap<Item,Integer> items) {
		this.maxWeight = maxWeigth;
		this.items = items;
	}
	
	public float getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
	}

	private PriorityQueue<Knapsack> getRandomPopulation(int populationSize) {
		PriorityQueue<Knapsack> population = new PriorityQueue<Knapsack>(populationSize,Collections.reverseOrder());
		for(int i=0; i<populationSize; i++) {
			float weight = 0f;
			float newWeight = 0f;
			Knapsack knapsack = new Knapsack(maxWeight, items.keySet());
			
			for(Entry<Item,Integer> item : items.entrySet()) {
				int quantity = rand.nextInt(item.getValue()+1);
				newWeight = weight + quantity;

				if(newWeight > maxWeight) {
					quantity = 0;
				} else {
					weight = newWeight;
				}
				try {
					knapsack.setQuantity(item.getKey(), quantity);
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			population.add(knapsack);
		}
		return population;
	}

	public Knapsack[] pollBests(PriorityQueue<Knapsack> population, int resultSize) {
		resultSize = Math.min(resultSize, population.size());
		if(resultSize<1) {
			return new Knapsack[0];
		}

		Knapsack[] result = new Knapsack[resultSize];
		for(int i=0; i<resultSize; i++) {
			result[i] = population.poll();
		}
		return result;
	}

	ArrayList<Knapsack> singlePointCrossover(Knapsack k1, Knapsack k2) {
		ArrayList<Knapsack> result = new ArrayList<>(2);
		
		ArrayList<HashMap<Item,Integer>> splittedK1 = k1.splitItemsList();
		ArrayList<HashMap<Item,Integer>> splittedK2 = k2.splitItemsList();

		Knapsack child1 = new Knapsack(maxWeight, items.keySet());
		Knapsack child2 = new Knapsack(maxWeight, items.keySet());

		try {		
			for(Entry<Item, Integer> item : splittedK1.get(0).entrySet()) {
				child1.setQuantity(item.getKey(), item.getValue());
			}

			for(Entry<Item, Integer> item : splittedK2.get(1).entrySet()) {
				child1.setQuantity(item.getKey(), item.getValue());
			}
			
			for(Entry<Item, Integer> item : splittedK2.get(0).entrySet()) {
				child2.setQuantity(item.getKey(), item.getValue());
			}
			
			for(Entry<Item, Integer> item : splittedK1.get(1).entrySet()) {
				child2.setQuantity(item.getKey(), item.getValue());
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		result.add(0, child1);
		result.add(1, child2);

		return result;
	}

	private ArrayList<Knapsack> breed(Knapsack parent1, Knapsack parent2) throws Exception {
		return singlePointCrossover(parent1, parent2);
	}

	private PriorityQueue<Knapsack> getNextPopulation(PriorityQueue<Knapsack> population) throws Exception {
		Knapsack[] bests = pollBests(population,4);
		population.clear();
		int p1 = 0;
		int p2 = 0;
		int dist = 0;

		population.add(bests[0]);
		population.add(bests[1]);
		population.add(bests[2]);
		
		// p1 & p2 change like that: 0-0; 0-1; 0-2, 1-1; 0-3, 1-2; 0-4, 1-3, 2-2;...
		do {
			ArrayList<Knapsack> children = breed(bests[p1], bests[p2]);
			for(int k=0; k<2; k++) {
				population.add(children.get(k));
			}
			p2++;
			p1--;
			if(p2>p1) {
				p2=0;
				dist++;
				p1=dist;
			}
		} while(population.size()<10 && p1<3 && p2<3);
		return population;
	}

	public PriorityQueue<Knapsack> mutate(PriorityQueue<Knapsack> population, float mutationFactor) {
		PriorityQueue<Knapsack> mutatedPopulation = new PriorityQueue<>(population.size(),Collections.reverseOrder());
		Random random = new Random();
		for(Knapsack knapsack : population) {
			// We mutate random items of the knapsack (if mutationFactor = 1 every quantity is changed and if it's 0 nothing is changed)
			for(int i=0; i<mutationFactor*knapsack.countUniqueItems(); i++) {
				Item randomItem = knapsack.getRandomItem();
				try {
					knapsack.setQuantity(randomItem, random.nextInt(knapsack.getMaxItemCopies()+1));
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				mutatedPopulation.add(knapsack);
			}
		}
		return mutatedPopulation;
	}

	public Knapsack train(int nGenerations, int populationSize) {
		PriorityQueue<Knapsack> population = getRandomPopulation(populationSize);
		
		System.out.println("Gen N°0 Score: "+population.peek().getScore()+" Weight: "+population.peek().getWeight()+" / "+getMaxWeight());
		for(int i=0; i<nGenerations; i++) {
			try {
				population = getNextPopulation(population);
				population = mutate(population,0.1f);
				System.out.println("Gen N°"+(i+1)+" Score: "+population.peek().getScore()+" Weight: "+population.peek().getWeight()+" / "+getMaxWeight());
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		return population.poll(); // Return the best knapsack
	}
}