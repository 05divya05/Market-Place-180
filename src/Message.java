import java.io.*;
import java.util.*;

public class Message implements MessageInterface {

    private static final String FILE="messages.txt";
    private String key(String a,String b){
        List<String> t=Arrays.asList(a,b); Collections.sort(t); return t.get(0)+","+t.get(1);
    }

    @Override public synchronized void send(String f,String t,String c)throws IOException{
        String k=key(f,t); boolean needKey=true;

        try(RandomAccessFile raf=new RandomAccessFile(FILE,"r")){
            long len=raf.length(); if(len>0){
                long p=len-1; while(p>0){
                    raf.seek(p--); if(raf.readByte()=='\n'){
                        String last=raf.readLine();
                        if(last!=null && last.equals(k)) needKey=false; break;
                    }
                }
            }
        }catch(FileNotFoundException ignore){}

        try(PrintWriter pw=new PrintWriter(new FileWriter(FILE,true))){
            if(needKey) pw.println(k);
            pw.println(f+": "+c);
        }
    }

    @Override public synchronized List<String> loadHistory(String u1,String u2)throws IOException{
        String k=key(u1,u2); List<String> res=new ArrayList<>();
        File f=new File(FILE); if(!f.exists()) return res;
        try(BufferedReader br=new BufferedReader(new FileReader(f))){
            String l; boolean ok=false;
            while((l=br.readLine())!=null){
                if(l.equals(k)){ ok=true; continue; }
                if(ok){ if(l.contains(",")) break; res.add(l); }
            }
        }
        return res;
    }
    @Override public synchronized List<String> listChats(String u)throws IOException{
        Set<String> ks=new LinkedHashSet<>();
        File f=new File(FILE); if(!f.exists()) return new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(f))){
            String l; while((l=br.readLine())!=null){
                if(l.contains(",")){
                    String[] p=l.split(",",2);
                    if(p[0].equals(u)||p[1].equals(u)) ks.add(l);
                }
            }
        }
        return new ArrayList<>(ks);
    }
}
