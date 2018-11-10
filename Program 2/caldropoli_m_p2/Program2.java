package caldropoli_m_p2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Program2 {
    public static void main(String[] args) throws FileNotFoundException {
        MinHeap mh = null;
        boolean first = true;
        int contestant = 0;
        int score = 0;
        String in = "";
        String line = "";
        String op = "";
        String out = "";

        if(args.length == 2) {
            in = args[0];
            out = args[1];
            if(in.indexOf('.') == -1) in = in + ".txt";
            if(out.indexOf('.') == -1) out = out + ".txt";
        } else {
            System.out.println("Incorrect Program Arguments");
            return;
        }

        //Print to output file
        PrintStream output = new PrintStream(new File(out));
        System.setOut(output);

        //Process input file
        try {
            File input = new File(in);
            FileReader inputReader = new FileReader(input);
            BufferedReader inputBuff = new BufferedReader(inputReader);
            while((line = inputBuff.readLine()) != null) {
                //Max size of heap
                if(first) {
                    mh = new MinHeap(Integer.parseInt(line));
                    first = false;
                    continue;
                }
                //Process operations
                System.out.println(line);
                if(line.indexOf(' ') == -1) {
                    op = line;

                    if(op.equals("eliminateWeakest")) mh.eliminateWeakest();
                    else if(op.equals("showContestants")) mh.showContestants();
                    else if(op.equals("showHandles")) mh.showHandles();
                    else if(op.equals("crownWinner")) mh.crownWinner();
                } else if(line.indexOf(' ') == line.lastIndexOf(' ')) {
                    int space = line.indexOf(' ');
                    op = line.substring(0,space);
                    contestant = Integer.parseInt(line.substring(space+2,space+3));

                    if(op.equals("findContestant")) mh.findContestant(contestant);
                    else if(op.equals("showLocation")) mh.showLocation(contestant);
                } else {
                    int space = line.indexOf(' ');
                    int arrow = line.lastIndexOf('<');
                    int arrow2 = line.lastIndexOf('>');
                    op = line.substring(0,space);
                    contestant = Integer.parseInt(line.substring(space+2,space+3));
                    score = Integer.parseInt(line.substring(arrow+1,arrow2));

                    if(op.equals("insertContestant")) mh.insertContestant(new Node(contestant,score));
                    else if(op.equals("earnPoints")) mh.earnPoints(contestant,score);
                    else if(op.equals("losePoints")) mh.losePoints(contestant,score);
                }
            }
            inputReader.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return;
        }
    }
}
