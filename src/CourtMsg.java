import java.io.Serializable;

public class CourtMsg implements Serializable {
    public int numbers;
    public Object[][] court;
    public CourtMsg(int n,Object[][] court){
        this.numbers=n;
        this.court=court;
    }

    public int getNumbers() {
        return numbers;
    }

    public Object[][] getCourt() {
        return court;
    }
}
