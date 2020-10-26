import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        SymbolTable st=new SymbolTable(71);
        List<Pair<String,Pair<Integer,Integer>>> pif=new ArrayList<>();
        try {
            LexicalAnalyzer LA=new LexicalAnalyzer();
            LA.scan("p3.txt",st,pif);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
