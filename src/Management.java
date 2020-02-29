import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class Management implements Serializable {
    private String userName;
    private char[] password;
    public String boss;
    public Management(String userName,char[] pw,String boss){
        this.userName=userName;
        password= Arrays.copyOf(pw,pw.length);
        this.boss=boss;
    }
    public String getUserName() {
        return userName;
    }
    public char[] getPassword() {
        return password;
    }

    public String getBoss() {
        return boss;
    }

    @Override
    public boolean equals(Object obj){
        if(getClass()!=obj.getClass())
            return false;
        Management p=(Management) obj;
        return Objects.equals(p.userName,userName)&&Arrays.equals(p.password,password);
    }
}
