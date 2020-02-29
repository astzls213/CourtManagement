import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;


public class Principle implements Serializable {
    private String userName;
    private char[] password;

    public Principle(String userName,char[] pw){
        this.userName=userName;
        password=Arrays.copyOf(pw,pw.length);
    }

    public String getUserName() {
        return userName;
    }

    public char[] getPassword() {
        return password;
    }
    @Override
    public boolean equals(Object obj){
        if(getClass()!=obj.getClass())
            return false;
        Principle p=(Principle) obj;
        return Objects.equals(p.userName,userName)&&Arrays.equals(p.password,password);
    }
}
