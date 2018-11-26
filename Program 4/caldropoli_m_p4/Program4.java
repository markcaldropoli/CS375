package caldropoli_m_p4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Program4 {
    private static Item[] items = null;
    private static String algo = "";
    private static int capacity = 0;
    private static int maxprofit = 0;
    private static int n = 0;
    private static int[] w = null;
    private static int[] p = null;
    private static int[][] table = null;

    public static void main(String[] args) throws FileNotFoundException {
        boolean first = true;
        int index = 0;
        int nProbs = 0;
        String in = "";
        String line = "";
        String out = "";

        if(args.length == 3) {
            in = args[0];
            out = args[1];
            algo = args[2];
            if(in.indexOf('.') == -1) in = in + ".txt";
            if(out.indexOf('.') == -1) out = out + ".txt";
        } else {
            System.out.println("Incorrect Program Arguments");
            return;
        }

        PrintStream output = new PrintStream(new File(out));
        System.setOut(output);

        try {
            File input = new File(in);
            FileReader inputReader = new FileReader(input);
            BufferedReader inputBuff = new BufferedReader(inputReader);
            while((line = inputBuff.readLine()) != null) {
                if(nProbs != 0 && nProbs == n) {
                    runAlgo(capacity);
                    first = true;
                    items = null;
                    capacity = 0;
                    index = 0;
                    nProbs = 0;
                    n = 0;
                }

                if(first) {
                    n = Integer.parseInt(line.substring(0,line.indexOf(' ')));
                    capacity = Integer.parseInt(line.substring(line.indexOf(' ')+1,line.length()));
                    items = new Item[n];
                    first = false;
                    continue;
                }
                int w = Integer.parseInt(line.substring(0,line.indexOf(' ')));
                int p = Integer.parseInt(line.substring(line.lastIndexOf(' ')+1,line.length()));
                items[index] = new Item(w,p);
                index++;
                nProbs++;
            }
            runAlgo(capacity);
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void runAlgo(int capacity) {
        long startTime;
        long endTime;
        long duration;
        quicksort(items,0,items.length-1);

        if(algo.equals("0")) {
            System.out.print(n + " ");
            startTime = System.nanoTime();
            System.out.print(greedy1(capacity) + " ");
            endTime = System.nanoTime();
            duration = endTime - startTime;
            System.out.print((duration/1000000.) + "\n");
        } else if(algo.equals("1")) {
            System.out.print(n + " ");
            startTime = System.nanoTime();
            int gmax = greedy1(capacity);
            System.out.print(greedy2(capacity,gmax) + " ");
            endTime = System.nanoTime();
            duration = endTime - startTime;
            System.out.print((duration/1000000.) + "\n");
        } else if(algo.equals("2")) {
            System.out.print(n + " ");
            startTime = System.nanoTime();
            int g1max = greedy1(capacity);
            maxprofit = greedy2(capacity,g1max);
            int i = 0;
            w = new int[items.length+1];
            p = new int[items.length+1];
            w[0] = 0;
            p[0] = 0;

            for(int x = 1; x < items.length; x++) {
                w[x] = items[x-1].getWeight();
                p[x] = items[x-1].getProfit();
            }

            knapsack(i,0,0);
            System.out.print(maxprofit + " ");
            endTime = System.nanoTime();
            duration = endTime - startTime;
            System.out.print((duration/1000000.) + "\n");
        } else if(algo.equals("3")) {
            //DYNAMIC PROGRAMMING CODE STARTS HERE
            startTime = System.nanoTime();
            table = new int[n+1][capacity+1];
            for(int i = 0; i < n+1; i++) {
                for(int j = 0; j < capacity+1; j++) {
                    table[i][j] = 0;
                }
            }

            w = new int[items.length+1];
            p = new int[items.length+1];
            w[0] = 0;
            p[0] = 0;
            for(int x = 1; x < items.length+1; x++) {
                w[x] = items[x-1].getWeight();
                p[x] = items[x-1].getProfit();
            }
            System.out.print("Dynamic Programming: " + n + " " + constructTable() + " ");
            endTime = System.nanoTime();
            duration = endTime - startTime;
            System.out.print((duration/1000000.) + "\n");
        } else System.out.println("Incorrect Algorithm # - Use 0/1/2/3");
    }

    public static void quicksort(Item[] arr, int begin, int end) {
        if(begin < end) {
            int q = partition(arr,begin,end);
            quicksort(arr,begin,q-1);
            quicksort(arr,q+1,end);
        }
    }

    public static int partition(Item[] arr, int begin, int end) {
        double x = arr[end].getRatio();
        int i = begin - 1;
        for(int j = begin; j < end; j++) {
            if(arr[j].getRatio() > x) {
                i++;
                Item temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Item temp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = temp;
        return (i+1);
    }

    public static int greedy1(int capacity) {
        int cap = 0;
        int maxprofit = 0;
        for(int i = 0; i < items.length && cap < capacity; i++) {
            if(items[i].getWeight()+cap < capacity) {
                cap += items[i].getWeight();
                maxprofit += items[i].getProfit();
            }
        }
        return maxprofit;
    }

    public static int greedy2(int capacity, int gmax) {
        int maxprofit = 0;
        for(int i = 0; i < items.length; i++) {
            if(items[i].getWeight() <= capacity && items[i].getProfit() > maxprofit) {
                maxprofit = items[i].getProfit();
            }
        }
        return Math.max(gmax,maxprofit);
    }

    public static void knapsack(int i, int profit, int weight) {
        if(weight <= capacity && profit > maxprofit) {
            maxprofit = profit;
        }
        
        if(isPromising(i)) {
            knapsack(i+1,profit+p[i+1],weight+w[i+1]);
            knapsack(i+1,profit,weight);
        }
    }

    public static boolean isPromising(int i) {
        if(w[i] >= capacity) return false;
        double bound = KWF2(i,w[i],p[i]);
        return (bound > maxprofit);
    }

    public static double KWF2(int i, int weight, int profit) {
        double bound = profit;

        while(weight < capacity && i <= n) {
            if(weight + w[i] <= capacity) {
                weight += w[i];
                bound += p[i];
            } else {
                bound += p[i]*((capacity*1.0 - weight*1.0)/w[i]*1.0);
                weight = capacity;
            }
            i++;
        }
        return bound;
    }

    /* DYNAMIC PROGRAMMING APPROACH */
    public static int constructTable() {
        for(int c = 0; c < capacity+1; c++) table[0][c] = 0;
        for(int i = 1; i < n+1; i++) {
            table[i][0] = 0;

            for(int c = 1; c < capacity+1; c++) {
                if(w[i] <= c && table[i-1][c-w[i]] + p[i] > table[i-1][c]) {
                    table[i][c] = table[i-1][c-w[i]] + p[i];
                } else {
                    table[i][c] = table[i-1][c];
                }
            }
        }
        return table[n][capacity];
    }

    public static void printTable() {
        for(int i = 0; i < n+1; i++) {
            for(int j = 0; j < capacity+1; j++) {
                System.out.print(table[i][j] + "|");
            }
            System.out.println();
        }
    }
}
