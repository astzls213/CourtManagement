public class Court {
    public String ID;
    public boolean status;
    public boolean free;
    public String Occupier;
    public int startTime;
    public int endTime;
    public double income;
    public String Manager;
    public String otherInfo;
    public double price;
    public Court(String ID,boolean status,boolean free,String occupier,int start,int end,double income,String manager,String info,double price){
        this.ID=ID;
        this.status=status;
        this.free=free;
        this.income=income;
        this.Occupier=occupier;
        this.otherInfo=info;
        this.Manager=manager;
        this.price=price;

    }
}
