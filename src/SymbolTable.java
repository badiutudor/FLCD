import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;

public class SymbolTable {
    private int m;
    private Node[] array;

    public SymbolTable(int m) {
        array = new Node[m];
        this.m=m;
        for (int i = 0; i < m; i++)
            array[i] = null;
    }

    public int h(Object val) {
        char ch[];
        String str = val.toString();
        ch = str.toCharArray();
        int length = str.length();
        int i, sum = 0;
        for (i = 0; i < str.length(); i++)
            sum += ch[i];
        return (sum+val.getClass().hashCode()) % m;
    }

    public Pair<Integer, Integer> search(Object token) {
        int pos = h(token), inlist = 0;
        String str = token.toString();
        Node crnt = array[pos];
        if (crnt == null) {
            return null;
        } else if (crnt.value.getClass() == token.getClass() && crnt.value.toString().equals(str)) {    // crnt.value.getClass() == token.getClass() && crnt.value.toString().equals(str)
            return new Pair<>(pos, inlist);
        }
        while (crnt.next != null) {
            crnt = crnt.next;
            inlist++;
            if (crnt.value.getClass() == token.getClass() && crnt.value.toString().equals(str))
                return new Pair<>(pos, inlist);
        }
        return null;
    }

    public Pair<Integer, Integer> insert(Object token) {
        int pos = h(token), inlist = 0;
        Node crnt = array[pos];
        if (crnt == null) {
            array[pos] = new Node(token);
            return new Pair<>(pos, 0);}
        while (crnt.next != null) {
            crnt = crnt.next;
            inlist++;
        }
        crnt.next = new Node(token);
        return new Pair<>(pos, inlist + 1);
        }
        public void WriteToFile(FileWriter fw) throws IOException {
            for(int i=0;i<m;i++){
                if(array[i]!=null){
                    String line=i+":(0)"+array[i].toString();
                    int inList=1;
                    Node crnt=array[i].next;
                    while(crnt!=null){
                        line=line+"->("+inList+")"+crnt.toString();
                        inList++;
                        crnt=crnt.next;
                    }
                    fw.write(line+"\n");
                }
            }
        }
}
