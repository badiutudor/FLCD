import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LexicalAnalyzer {
    private List<String> operators=new ArrayList<>();
    private List<String> separators=new ArrayList<>();
    private List<String> reserved=new ArrayList<>();
    private FA identifierFA=new FA("FAidentifier.in");
    private FA intFA=new FA("FAinteger.in");
    public LexicalAnalyzer() throws FileNotFoundException{
        File input=new File("token.in");
        Scanner reader=new Scanner(input);
        operators.addAll(Arrays.asList(reader.nextLine().split(" ")));
        separators.addAll(Arrays.asList(reader.nextLine().split(" ")));
        reserved.addAll(Arrays.asList(reader.nextLine().split(" ")));
    }
    private boolean diginumeric(char x){
        return (x>='A'&&x<='Z')||(x>='a'&&x<='z')||(x>='0'&&x<='9');
    }
    private String[] checkString(String line) throws Error{
        if(line.charAt(0)=='"'){
            int i=1;
            while(line.length()>i && line.charAt(i)!='"')
                i++;
            if(line.length()==i)
                throw new Error(line);
            return new String[]{line.substring(i+1),line.substring(0,i+1),"string"};
    }
        return null;
    }
    private String[] checkIntFA(String line) throws Error{
        String res=intFA.verifySequenceSUBSTRING(line);
        if(res==null)
            return null;
        if(res.length()<line.length() && diginumeric(line.charAt(res.length())))
            throw new Error(line);
        return new String[]{line.substring(res.length()),res,"identifier"};
    }
    private String[] checkInt(String line) throws Error{
        if(line.charAt(0)=='+'||line.charAt(0)=='-'){
            int i=1;
            if(line.length()>i && line.charAt(i)>'0'&&line.charAt(i)<='9'){
                i++;
                while(line.length()>=i && line.charAt(i)>='0'&&line.charAt(i)<='9')
                    i++;
                if(line.length()>i && (line.charAt(i)>='A'&&line.charAt(i)<='Z'||(line.charAt(i)>='a'&&line.charAt(i)<='z')))
                    throw new Error(line);
                return new String[]{line.substring(i),line.substring(0,i),"int"};
            }
        }
        if(line.charAt(0)>'0'&&line.charAt(0)<='9'){
            int i=1;
            while(line.length()>=i && line.charAt(i)>='0'&&line.charAt(i)<='9')
                i++;
            if(line.length()>i && (line.charAt(i)>='A'&&line.charAt(i)<='Z'||(line.charAt(i)>='a'&&line.charAt(i)<='z')))
                throw new Error(line);
            return new String[]{line.substring(i),line.substring(0,i),"int"};
        }
        if(line.charAt(0)=='0')
            if(line.length()==1 || !diginumeric(line.charAt(1)))
                return new String[]{line.substring(1),"0","int"};
            else
                throw new Error(line);
        return null;
    }
    private String[] checkIdentifier(String line) throws Error{
        if(line.charAt(0)>='A'&&line.charAt(0)<='Z'||(line.charAt(0)>='a'&&line.charAt(0)<='z')){
            int i=1;
            while(line.length()>=i && (line.charAt(i)=='_' || diginumeric(line.charAt(i))))
                i++;
            return new String[]{line.substring(i),line.substring(0,i),"identifier"};
        }
        return null;
    }
    private String[] checkIdentifierFA(String line) throws Error{
        String res=identifierFA.verifySequenceSUBSTRING(line);
        if(res==null)
            return null;
        return new String[]{line.substring(res.length()),res,"identifier"};
    }
    private String[] nextToken(String line) throws Error{
        while(line.length()!=0 && (line.charAt(0)==' '||line.charAt(0)=='\t'))
            line=line.substring(1);
        if(line.equals(""))
            return null;
        String[] result=null; // rest of line,token,type
        result=checkString(line);
        if(result!=null)
            return result;
        result=checkIntFA(line);
        if(result!=null)
            return result;
        for(String op:operators){
            if(line.indexOf(op)==0)
                return new String[]{line.substring(op.length()),op,"operator"};
        }
        for(String sp:separators){
            if(line.indexOf(sp)==0)
                return new String[]{line.substring(sp.length()), sp, "separator"};
        }
        for(String res:reserved){
            if(line.indexOf(res)==0 &&(line.length()==res.length() || line.charAt(res.length())==' ' ||(!diginumeric(line.charAt(res.length()))))){
                if(res.equals("true")||res.equals("false"))
                    return new String[]{line.substring(res.length()),res,"bool"};
                return new String[]{line.substring(res.length()),res,"reserved"};
            }
        }
        result=checkIdentifierFA(line);
        if(result!=null)
            return result;
        throw new Error(line);
    }
    public void writeFiles(SymbolTable st,List<Pair<String,Pair<Integer,Integer>>> pif){
        try{
            FileWriter stF=new FileWriter("ST.out");
            FileWriter pifF=new FileWriter("PIF.out");
            st.WriteToFile(stF);
            for (Pair<String, Pair<Integer, Integer>> pair : pif) {
                if(pair.getValue()!=null)
                    pifF.write(pair.getKey() + " (" + pair.getValue().getKey() + "," + pair.getValue().getValue() + ")\n");
                else
                    pifF.write(pair.getKey() + " 0\n");
            }
            stF.close();
            pifF.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void scan(String file, SymbolTable st, List<Pair<String,Pair<Integer,Integer>>> pif){
        File input=new File(file);
        Scanner reader=null;
        try{
            reader=new Scanner(input);
        }catch(FileNotFoundException e){
            System.out.println("file not found");
            return;
        }
        int currentline=0;
        while(reader.hasNextLine()){
            currentline++;
            String line=reader.nextLine(),token,type;
            String[] result={"startline"};
            while(true){
                try{
                    result=nextToken(line);
                    if(result==null)
                        break;
                    else{
                        System.out.println(result[1]);
                        line=result[0];
                        token=result[1];
                        type=result[2];
                        //System.out.println(line+"---"+token+"---"+type);
                        if("operator,separator,reserved".contains(type)){
                            pif.add(new Pair<>(token,null));
                        } else{
                            Object tk=null;
                            switch(type){
                                case "identifier":
                                    tk=new Identifier(token);
                                    break;
                                case "bool":
                                    tk=Boolean.valueOf(token);
                                    break;
                                case "int":
                                    tk= Integer.valueOf(token);
                                    break;
                                case "string":
                                    tk=token;
                                    break;
                            }
                            Pair<Integer,Integer> pos=st.search(tk);
                            if(pos==null)
                                pos=st.insert(tk);
                            if(type.equals("identifier"))
                                pif.add(new Pair<>("ID",pos));
                            else
                                pif.add(new Pair<>("CONST",pos));
                        }
                    }
                }catch(Error e){
                    System.out.println("Lexical error on line"+ currentline+" : "+e.getMessage());
                    writeFiles(st,pif);
                    return;
                }

            }
        }
        System.out.println("lexically correct");
        writeFiles(st,pif);
    }

}
