package caldropoli_m_p3;

public class Item {
    private double ratio;
    private int profit;
    private int weight;

    public Item(int weight, int profit) {
        this.weight = weight;
        this.profit = profit;
        this.ratio = (profit*1.0)/weight;
    }

    public int getProfit() {
        return profit;
    }

    public double getRatio() {
        return ratio;
    }

    public int getWeight() {
        return weight;
    }
}
