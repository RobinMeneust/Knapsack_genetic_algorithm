package knapsack;

public class Item {
	private String name;
	private float weight;
	private float value;

	public Item() {
		name = "unamed";
		weight = 0.0f;
		value = 0.0f;
	}

	public Item(String name, float weight, float value) {
		this.name = name;
		this.weight = weight;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public float getWeight() {
		return weight;
	}

	public float getValue() {
		return value;
	}

	public String toString() {
		return getName() + " : $" + getValue() + " and " + getWeight() + "kg";
	}
}