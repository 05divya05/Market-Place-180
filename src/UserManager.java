import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class UserManager implements UserManagerInterface {

    private static final String USER_FILE = "users.txt";   // username,email,password,balance
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public synchronized boolean register(String u,String e,String p){
        if(u.isBlank()||e.isBlank()||p.length()<6) return false;
        if(!EMAIL.matcher(e).matches()) return false;
        if(userExists(u)||emailExists(e)) return false;
        try(PrintWriter pw=new PrintWriter(new FileWriter(USER_FILE,true))){
            pw.println(u+","+e+","+p+",100.0");
            return true;
        }catch(IOException ignore){return false;}
    }

    @Override
    public synchronized boolean login(String u,String e,String p){
        try(BufferedReader br=new BufferedReader(new FileReader(USER_FILE))){
            String l; while((l=br.readLine())!=null){
                String[] f=l.split(",",4);
                if(f.length==4 && f[0].equals(u)&&f[1].equals(e)&&f[2].equals(p))
                    return true;
            }
        }catch(IOException ignore){}
        return false;
    }

    @Override
    public synchronized boolean deleteAccount(String u,String e,String p){
        List<String> keep=new ArrayList<>(); boolean removed=false;
        try(BufferedReader br=new BufferedReader(new FileReader(USER_FILE))){
            String l; while((l=br.readLine())!=null){
                String[] f=l.split(",",4);
                if(f[0].equals(u)&&f[1].equals(e)&&f[2].equals(p)){removed=true;continue;}
                keep.add(l);
            }
        }catch(IOException ignore){}
        if(!removed) return false;
        try(PrintWriter pw=new PrintWriter(new FileWriter(USER_FILE))){
            for(String s:keep) pw.println(s);
            return true;
        }catch(IOException ignore){return false;}
    }

    @Override public synchronized double getBalance(String u){
        try(BufferedReader br=new BufferedReader(new FileReader(USER_FILE))){
            String l; while((l=br.readLine())!=null){
                String[] f=l.split(",",4);
                if(f[0].equals(u)) return Double.parseDouble(f[3]);
            }
        }catch(IOException ignore){}
        return 0;
    }
    @Override public synchronized boolean setBalance(String u,double b){
        List<String> ls=new ArrayList<>(); boolean ok=false;
        try(BufferedReader br=new BufferedReader(new FileReader(USER_FILE))){
            String l; while((l=br.readLine())!=null){
                String[] f=l.split(",",4);
                if(f[0].equals(u)){f[3]=String.format("%.2f",b);ok=true;l=String.join(",",f);}
                ls.add(l);
            }
        }catch(IOException ignore){}
        if(!ok) return false;
        try(PrintWriter pw=new PrintWriter(new FileWriter(USER_FILE))){
            for(String s:ls) pw.println(s);
            return true;
        }catch(IOException ignore){return false;}
    }

    @Override public boolean userExists (String u){return check(0,u);}
    @Override public boolean emailExists(String e){return check(1,e);}
    private boolean check(int idx,String val){
        try(BufferedReader br=new BufferedReader(new FileReader(USER_FILE))){
            String l; while((l=br.readLine())!=null){
                String[] f=l.split(",",idx+2);
                if(f[idx].equals(val)) return true;
            }
        }catch(IOException ignore){}
        return false;
    }
}
