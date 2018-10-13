package caldropoli_m_p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import javafx.util.Pair;

public class Program1 {
    public static void main(String[] args) throws FileNotFoundException {
        boolean firstLine = true;
        int budget = 0;
        int index = 0;
        int inputSize = 0;
        int numCards = 0;
        String line = "";
        String mpf = "";
        String plf = "";

        //All prints sent to output.txt
        PrintStream output = new PrintStream(new File("output.txt"));
        System.setOut(output);

        //Parse Input
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-m")) {
                if(i < args.length-1 && args[i+1].charAt(0) != '-') {
                    mpf = args[++i];
                } else {
                    System.out.println("Missing Market Price File Name");
                    return;
                }
            }

            if(args[i].equals("-p")) {
                if(i < args.length-1 && args[i+1].charAt(0) != '-') {
                    plf = args[++i];
                } else {
                    System.out.println("Missing Price List File Name");
                    return;
                }
            }
        }

        //Check for missing -m
        if(mpf.equals("")) {
            System.out.println("Missing -m tag");
            return;
        }

        //Check for missing -p
        if(plf.equals("")) {
            System.out.println("Missing -p tag");
            return;
        }

        ArrayList<Pair<String, Integer>> mv = new ArrayList<Pair<String, Integer>>(); //holds MPF content
        ArrayList<Pair<String, Integer>> cards = new ArrayList<Pair<String, Integer>>(); //holds LFP content

        //Read in Market Price File
        try {
            File mpfFile = new File(mpf);
            FileReader mpfReader = new FileReader(mpfFile);
            BufferedReader mpfBuff = new BufferedReader(mpfReader);
            while((line = mpfBuff.readLine()) != null) {
                if(firstLine) {
                    inputSize = Integer.parseInt(line);
                    firstLine = false;
                    continue;
                }
                String str = line.substring(0, line.indexOf(' '));
                int x = Integer.parseInt(line.substring(line.indexOf(' ')+1, line.length()));
                mv.add(new Pair<String, Integer>(str, x));
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        firstLine = true;
        long startTime;
        long endTime;
        long duration;
 
        //Process Price List File Problems
        try {
            File plfFile = new File(plf);
            FileReader plfReader = new FileReader(plfFile);
            BufferedReader plfBuff = new BufferedReader(plfReader);
            startTime = System.nanoTime();
            while((line = plfBuff.readLine()) != null) {
                //Reached end of a problem
                if(index != 0 && index == numCards) {
                    Pair<Integer, ArrayList<String>> result = computeMaxProfit(cards, budget, mv);
                    System.out.print(result.getKey() + " ");
                    System.out.print(result.getValue().size() + " ");
                    cards = new ArrayList<Pair<String, Integer>>();
                    firstLine = true;
                    index = 0;
                    endTime = System.nanoTime();
                    duration = endTime - startTime;
                    System.out.print((duration/1000000000.) + "\n");
                    startTime = System.nanoTime();

                    for(int i = 0; i < result.getValue().size(); i++) System.out.println(result.getValue().get(i));
                    System.out.println();
                }

                //Beginning of a problem
                if(firstLine) {
                    numCards = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                    if(numCards == 0) {
                        endTime = System.nanoTime();
                        duration = endTime - startTime;
                        System.out.println("0 0 0 " + (duration/1000000000.) + "\n");
                        startTime = System.nanoTime();
                        continue;
                    }
                    System.out.print(numCards + " ");
                    budget = Integer.parseInt(line.substring(line.indexOf(' ')+1, line.length()));
                    firstLine = false;
                    continue;
                }

                //Process file contents into program
                String str = line.substring(0, line.indexOf(' '));
                int x = Integer.parseInt(line.substring(line.indexOf(' ')+1, line.length()));
                cards.add(new Pair<String, Integer>(str, x));
                index++;
            }

            //Process last problem
            if(numCards != 0) {
                Pair<Integer, ArrayList<String>> result = computeMaxProfit(cards, budget, mv);
                System.out.print(result.getKey() + " ");
                System.out.print(result.getValue().size() + " ");
                endTime = System.nanoTime();
                duration = endTime - startTime;
                System.out.print((duration/1000000000.) + "\n");
                
                for(int i = 0; i < result.getValue().size(); i++) System.out.println(result.getValue().get(i));
                System.out.println();
            }

            plfReader.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public static Pair<Integer, ArrayList<String>> computeMaxProfit(ArrayList<Pair<String, Integer>> items, int weight, ArrayList<Pair<String, Integer>> market) {
        int maxProfit = 0;
        int expenses = 0;
        ArrayList<String> max = new ArrayList<>();
        
        for(Pair<String, Integer> pair : items) expenses += pair.getValue();

        if(expenses <= weight) {
            for(Pair<String, Integer> pair : items) max.add(pair.getKey());

            //Calculate income of purchasing cards using market prices
            for(int i = 0; i < max.size(); i++) {
                for(int j = 0; j < market.size(); j++) {
                    if(max.get(i).equals(market.get(j).getKey())) {
                        maxProfit += market.get(j).getValue();
                        break;
                    }
                }
            }

            maxProfit -= expenses;
            return new Pair<Integer, ArrayList<String>>(maxProfit, max);
        }

        int size = items.size();
        ArrayList<Pair<String, Integer>> arr = new ArrayList<>();
        expenses = 0;

        //Generate each subset using bit manipulation
        for(int i = 0; i < (1 << size); i++) {
            for(int j = 0; j < size; j++) {
                if( (i & (1 << j)) > 0) arr.add(items.get(j));
            }
            
            for(Pair<String, Integer> pair : arr) expenses += pair.getValue();

            if(expenses <= weight) {
                //Calculate profit of current subset
                int profit = 0;
                for(int x = 0; x < arr.size(); x++) {
                    for(int y = 0; y < market.size(); y++) {
                        if(arr.get(x).getKey().equals(market.get(y).getKey())) {
                            profit += market.get(y).getValue();
                            break;
                        }
                    }
                }

                profit -= expenses;

                //Check for a new max profit
                if(profit > maxProfit) {
                    maxProfit = profit;
                    max = new ArrayList<String>();
                    for(int x = 0; x < arr.size(); x++) max.add(arr.get(x).getKey());
                }
            }

            //Reset for next subset
            expenses = 0;
            arr = new ArrayList<Pair<String, Integer>>();
        }

        return new Pair<Integer, ArrayList<String>>(maxProfit, max);
    }
}
