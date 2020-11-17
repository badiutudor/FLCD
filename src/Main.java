import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    //Review: not much to review...almost perfect :D
    public static void main(String[] args) throws IOException {
        /*SymbolTable st=new SymbolTable(13);
        List<Pair<String,Pair<Integer,Integer>>> pif=new ArrayList<>();
        try {
            LexicalAnalyzer LA=new LexicalAnalyzer();
            LA.scan("p3.txt",st,pif);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        FA fa=new FA("FA.in");
        fa.ConsoleMenu();



    }
}
