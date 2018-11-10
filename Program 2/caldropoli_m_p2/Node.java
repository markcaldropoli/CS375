package caldropoli_m_p2;

public class Node implements Comparable<Node> {
    private int id;
    private int score;

    public Node(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.score, o.score);
    }
}
