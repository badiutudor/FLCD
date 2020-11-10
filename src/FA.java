import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FA{
    //Representation: states,finalstates and alphabet stored as Sets of strings
    //                transitions stored in Map as key:Pair<String:startstate,Char:TransitionSymbol> value:String:endstate
    //                deterministic flag is set automatically when reading a new FA
    public boolean deterministic=true;
    public String q0;
    public Set<String> states;
    public Set<String> finalstates;
    public Set<String> alphabet;
    public Map<Pair<String,Character>, List<String>> transitions;
    //the FA is read during initialisation
    public FA(String filename) throws FileNotFoundException {
        states=new TreeSet<String>();
        finalstates=new TreeSet<String>();
        alphabet=new TreeSet<String>();
        transitions=new HashMap<Pair<String,Character>,List<String>>();
        reloadFA(filename);
    }
    public void reloadFA(String filename) throws FileNotFoundException {
        Scanner reader=new Scanner(new File(filename));
        q0=reader.nextLine();
        states.addAll(Arrays.asList(reader.nextLine().split(",")));
        finalstates.addAll(Arrays.asList(reader.nextLine().split(",")));
        alphabet.addAll(Arrays.asList(reader.nextLine().split(",")));
        while(reader.hasNextLine()){
            String[] trans=reader.nextLine().split(",");
            Pair<String,Character> key=new Pair<String,Character>(trans[0],trans[1].charAt(0));
            if(transitions.containsKey(key))
                deterministic=false;
            else
                transitions.put(key,new ArrayList<>());
            transitions.get(key).add(trans[2]);
        }
        reader.close();
    }
    public void ConsoleMenu(){
        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("0.Terminate | 1.States | 2.Alphabet | 3. Transitions | 4.Final States | 5.Is deterministic?");
            int x=scan.nextInt();
            switch(x){
                case 0:
                    scan.close();
                    return;
                case 1:
                    System.out.println(states.toString());
                    break;
                case 2:
                    System.out.println(alphabet.toString());
                    break;
                case 3:
                    transitions.forEach((key, value) -> System.out.println("(" + key.getKey() + "," + key.getValue() + ")->" + value.toString()));
                    break;
                case 4:
                    System.out.println(finalstates.toString());
                    break;
                case 5:
                    scan.nextLine();
                    System.out.println(verifySequence(scan.nextLine()));
                    break;
            }
        }
    }
    //checks if whole input string is a valid sequence
    public boolean verifySequence(String sequence){
        if(!deterministic)
            throw new Error("not a DFA");
        String state=q0;
        sequence=sequence.trim();
        while(!sequence.equals("")){
            List<String> nextstate=transitions.get(new Pair<>(state,sequence.charAt(0)));
            if(nextstate==null)
                return false;
            sequence=sequence.substring(1);
            state=nextstate.get(0);
        }
        return finalstates.contains(state);
    }

    public String verifySequenceSUBSTRING(String sequence){ //returns the longest viable sequence substring
        if(!deterministic)
            throw new Error("not a DFA");
        String state=q0;
        String copy=sequence;
        int currentchar=0;
        while(currentchar<copy.length()&&alphabet.contains(Character.toString(copy.charAt(currentchar)))){
            List<String> nextstate=transitions.get(new Pair<>(state,copy.charAt(currentchar)));
            if(nextstate==null)
                return null;
            currentchar++;
            state=nextstate.get(0);
        }
        if(finalstates.contains(state))
            return copy.substring(0,currentchar);
        return null;
    }
}