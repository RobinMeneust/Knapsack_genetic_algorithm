package knapsack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class Knapsack implements Comparable<Knapsack> {
	private HashMap<Item,Integer> items;
	private float maxWeight;
	private float weight;
	private int maxItemCopies;

	public Knapsack(float maxWeight, Set<Item> itemsList) {
		this.maxItemCopies = 1;
		this.items = new HashMap<>();
		for(Item item : itemsList) {
			this.items.put(item,0);
		}
		this.maxWeight = maxWeight;
		this.weight = 0.0f;
		this.maxItemCopies = 1;
	}

	public boolean contains(Item item) {
		return getQuantity(item)>0;
	}

	public int getQuantity(Item item) {
		if(!items.keySet().contains(item)) {
			return 0;
		} else {
			return items.get(item);
		}
	}

	protected void setQuantity(Item item, int quantity) throws Exception {
		if(items.keySet().contains(item)) {
			quantity = Math.max(quantity,0);
			int prevQuantity = items.get(item);
			items.replace(item,quantity);
			setWeight(getWeight()+(quantity-prevQuantity)*item.getWeight());
		} else {
			throw new Exception("Unknown item");
		}
	}

	public int countUniqueItems() {
		return items.keySet().size();
	}

	protected Item getRandomItem() {
		Random random = new Random();
		int index = random.nextInt(items.keySet().size());
		Item[] itemsArray = new Item[items.keySet().size()];
		itemsArray = items.keySet().toArray(itemsArray);

		return itemsArray[index];
	}

	protected ArrayList<HashMap<Item, Integer>> splitItemsList() {
		ArrayList<HashMap<Item, Integer>> result = new ArrayList<>(2);

		HashMap<Item,Integer> firstHalf = new HashMap<>();
		HashMap<Item,Integer> secondHalf = new HashMap<>();

		Item[] itemsArray = new Item[items.keySet().size()];
		itemsArray = items.keySet().toArray(itemsArray);
		for(int i=0; i<itemsArray.length/2; i++) {
			firstHalf.put(itemsArray[i], getQuantity(itemsArray[i]));
		}
		for(int i=itemsArray.length/2; i<itemsArray.length; i++) {
			secondHalf.put(itemsArray[i], getQuantity(itemsArray[i]));
		}
		result.add(0,firstHalf);
		result.add(1,secondHalf);

		return result;
	}

	public float getMaxWeight() {
		return maxWeight;
	}

	public int getMaxItemCopies() {
		return maxItemCopies;
	}

	public float getWeight() {
		return weight;
	}

	private void setWeight(float weight) {
		this.weight = weight;
	}

	public String toString() {
		if(getWeight()==0) {
			return "Knapsack (0kg / "+getMaxWeight()+"kg) : Empty";
		}
		String str = "Knapsack content: ("+getWeight()+"kg / "+getMaxWeight()+"kg)";
		for(Item item : items.keySet()) {
			if(getQuantity(item)>0) {
				str += "\n- "+item;
			}
		}
		return str;
	}

	public float getScore() {
		float score = 0f;

		if(getWeight()>getMaxWeight()) {
			return 0f;
		}

		for(Entry<Item,Integer> item : items.entrySet()) {
			score += item.getKey().getValue() * item.getValue(); // value * quantity
		}

		return score;
	}

	@Override
	public int compareTo(Knapsack k) {
		if(this.getScore() < k.getScore()) {
			return -1;
		} else if(this.getScore() == k.getScore()) {
			return 0;
		} else {
			return 1;
		}
	}
}
