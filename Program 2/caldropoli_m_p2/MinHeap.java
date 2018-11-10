package caldropoli_m_p2;

import java.util.*;

public class MinHeap {
    private HashMap<Integer,Node> nodes = new HashMap<>();
    private PriorityQueue<Node> pq;
    private int capacity;
    private int size = 0;

    public MinHeap(int capacity) {
        pq = new PriorityQueue<Node>(capacity);
        this.capacity = capacity;
    }

    public boolean findContestant(int id) {
        if(nodes.get(id) != null) {
            System.out.println("Contestant <" + id + "> is in the extended heap with score <" + nodes.get(id).getScore() + ">.");
            return true;
        }

        System.out.println("Contestant <" + id + "> is not in the extended heap.");
        return false;
    }

    public void insertContestant(Node n) {
        if(size >= capacity) {
            System.out.println("Contestant " + n.getId() + " could not be inserted because the extended heap is full.");
            size++;
            return;
        } else if(nodes.get(n.getId()) != null) {
            System.out.println("Contestant " + n.getId() + " is already in the extended heap: cannot insert.");
            return;
        }
        pq.add(n);
        nodes.put(n.getId(),n);
        size++;
        System.out.println("Contestant <" + n.getId() + "> inserted with initial score <" + n.getScore() + ">.");
    }

    public void eliminateWeakest() {
        Node n = pq.poll();
        if(n != null) {
            System.out.println("Contestant <" + n.getId() + "> with current lowest score <" + n.getScore() + "> eliminated.");
            nodes.remove(n.getId());
            nodes.put(-1, n);
            size--;
        }
        else System.out.println("No contestant can be eliminated since the extended heap is empty.");
    }

    public void earnPoints(int id, int points) {
        if(nodes.get(id) != null) {
            int score = 0;
            Node rem = null;
            Node[] list = new Node[capacity];
            Iterator<Node> it = pq.iterator();
            while(it.hasNext()) {
                Node n = it.next();
                if(n.getId() == id) {
                    score = n.getScore() + points;
                    rem = n;
                    break;
                }
            }
            pq.remove(rem);
            Node newNode = new Node(id,score);
            pq.add(newNode);
            nodes.replace(id,newNode);
            int index = 0;
            while(!pq.isEmpty()) {
                list[index] = pq.poll();
                index++;
            }
            for(int i = 0; i < list.length && list[i] != null; i++) {
                pq.add(list[i]);
                nodes.replace(i+1,list[i]);
            }
            System.out.println("Contestant <" + id + ">s score increased by <" + points + "> points to <" + score + ">.");
            return;
        }
        System.out.println("Contestant <" + id + "> is not in the extended heap.");
    }

    public void losePoints(int id, int points) {
        if(nodes.get(id) != null) {
            int score = 0;
            Node rem = null;
            Node[] list = new Node[capacity];
            Iterator<Node> it = pq.iterator();
            while(it.hasNext()) {
                Node n = it.next();
                if(n.getId() == id) {
                    score = n.getScore() - points;
                    rem = n;
                    break;
                }
            }
            pq.remove(rem);
            Node newNode = new Node(id,score);
            pq.add(newNode);
            nodes.replace(id,newNode);
            int index = 0;
            while(!pq.isEmpty()) {
                list[index] = pq.poll();
                index++;
            }
            for(int i = 0; i < list.length && list[i] != null; i++) {
                pq.add(list[i]);
                nodes.replace(i+1,list[i]);
            }
            System.out.println("Contestant <" + id + ">s score decreased by <" + points + "> points to <" + score + ">.");
            return;
        }
        System.out.println("Contestant <" + id + "> is not in the extended heap.");
    }

    public void showContestants() {
        Iterator<Node> it = pq.iterator();
        int loc = 1;
        while(it.hasNext()) {
            Node n = it.next();
            System.out.println("Contestant <" + n.getId() + "> in extended heap location <" + loc + "> with score <" + n.getScore() + ">.");
            loc++;
        }
    }

    public void showHandles() {
        Iterator<Node> it = pq.iterator();
        int[] list = new int[capacity+1];
        int loc = 1;
        while(it.hasNext()) {
            Node n = it.next();
            list[n.getId()] = loc;
            loc++;
        }
        for(int i = 1; i < capacity+1; i++) {
            if(list[i] == 0) System.out.println("There is no Contestant <" + i + "> in the extended heap: handle[<" + i + ">] = -1.");
            else System.out.println("Contestant <" + i + "> stored in extended heap location <" + list[i] + ">.");
        }
    }

    public void showLocation(int id) {
        Iterator<Node> it = pq.iterator();
        int[] list = new int[capacity+1];
        int loc = 1;
        while(it.hasNext()) {
            Node n = it.next();
            if(n.getId() == id) {
                System.out.println("Contestant <" + id + "> stored in extended heap location <" + loc + ">.");
                return;
            }
            loc++;
        }
        System.out.println("There is no Contestant <" + id + "> in the extended heap: handle[<" + id + ">] = -1.");
    }

    public void crownWinner() {
        Node n = null;
        while(!pq.isEmpty()) n = pq.poll();
        System.out.println("Contestant <" + n.getId() + "> wins with score <" + n.getScore() + ">!");
    }
}
