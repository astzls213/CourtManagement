import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class ManagementSystem {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            File bossInfo=new File("负责人信息.txt");
            File manInfo=new File("管理人员信息.txt");
            try{
                //create this file.
                boolean flag1=bossInfo.createNewFile();
                boolean flag2=manInfo.createNewFile();
                if(flag1){

                }
                if(flag2){
                    ObjectOutputStream oos= null;
                    try {
                        oos = new ObjectOutputStream(new FileOutputStream("管理人员信息.txt"));
                        Set<Management> managerInfo=new HashSet<>();
                        char[] pw={'r','o','o','t'};
                        managerInfo.add(new Management("root", pw,"root"));
                        oos.writeObject(managerInfo);
                        oos.close();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            //来到登陆界面
            Log log=new Log();
            log.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            log.setVisible(true);
        });
    }
}
