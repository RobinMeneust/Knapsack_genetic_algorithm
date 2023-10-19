package knapsack;

import java.util.HashMap;
import java.util.Random;

public class Main {
    public static HashMap<Item,Integer> randomItemsGen(int size) {
        Random random = new Random();
        HashMap<Item,Integer> items = new HashMap<>();
        for(int i=0; i<size; i++) {
            items.put(new Item("item"+i,random.nextFloat(10),random.nextFloat(20)),1);
        }
        return items;
    }

    public static void main(String[] args) {
		Item A = new Item("A", 24f, 12f);
		Item B = new Item("B", 10f, 9f);
        Item C = new Item("C", 10f, 9f);
        Item D = new Item("D", 7f, 5f);

        HashMap<Item,Integer> items = new HashMap<>(2);
        items.put(A, 1);
        items.put(B, 1);
        items.put(C, 1);
        items.put(D, 1);

        GeneticAlgo geneticAlgo = new GeneticAlgo(20f, items);
        
        Knapsack result = geneticAlgo.train(5, 10);
        System.out.println("result:\n"+result);

        ///////////////////////

        int nbItems = 70;
        GeneticAlgo geneticAlgo2 = new GeneticAlgo(10+5*nbItems, randomItemsGen(nbItems));

        Knapsack result2 = geneticAlgo2.train(10, 20);
    }
}
