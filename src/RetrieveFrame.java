import javax.print.attribute.standard.NumberUp;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipException;

public class RetrieveFrame extends JFrame {
    public RetrieveFrame(String boss){
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();
        setTitle("收回信息");
        JLabel prompt=new JLabel("请输入场地编号：",SwingConstants.RIGHT);
        JTextField id=new JTextField(8);
        JButton ok=new JButton("OK");

        setLocationRelativeTo(null);
        p1.add(prompt);
        p1.add(id);
        p2.add(ok);
        add(p1,BorderLayout.NORTH);
        add(p2,BorderLayout.SOUTH);
        pack();
        ok.addActionListener(event->{
            try{
                //读取球场信息
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream(boss+"的球场.txt"));
                CourtMsg cm=(CourtMsg)ois.readObject();
                ois.close();
                //找到指定球场
                int find=-1;
                for(int i=0;i<cm.numbers;i++){
                    if(cm.court[i][0].equals(id.getText()))
                        find=i;
                }
                if(find==-1)
                    throw new IOException();
                //判断是否被租了
                if((Boolean)cm.court[find][2]){
                    throw new ZipException();
                }
                //修改信息
                cm.court[find][3]="";
                cm.court[find][8]="";
                cm.court[find][2]=true;
                //计算金额
                LocalDateTime cur=LocalDateTime.now();
                if(cur.isAfter((LocalDateTime)cm.court[find][4]))
                {
                    if(((LocalDateTime)cm.court[find][5]).isAfter(cur)){
                        int hours=cur.getHour()-((LocalDateTime)cm.court[find][4]).getHour();
                        int minute=cur.getMinute()-((LocalDateTime)cm.court[find][4]).getMinute();
                        double price=Double.parseDouble(cm.court[find][7].toString());
                        double total=(hours+(minute/60.0))*price;
                        double remain=Double.parseDouble(cm.court[find][6].toString());
                        cm.court[find][6]=total+remain;
                    }
                    else{
                        int hours=((LocalDateTime)cm.court[find][5]).getHour()-((LocalDateTime)cm.court[find][4]).getHour();
                        int minute=((LocalDateTime)cm.court[find][5]).getMinute()-((LocalDateTime)cm.court[find][4]).getMinute();
                        double price=Double.parseDouble(cm.court[find][7].toString());
                        double total=(hours+(minute/60.0))*price;
                        double remain=Double.parseDouble(cm.court[find][6].toString());
                        cm.court[find][6]=total+remain;
                    }
                }
                cm.court[find][4]=null;
                cm.court[find][5]=null;
                //保存修改
                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(boss+"的球场.txt"));
                oos.writeObject(cm);
                oos.close();
                //关闭窗口
                dispose();
            }
            catch (ZipException e){
                JOptionPane.showMessageDialog(null,"该场地正空闲，收回错误","消息",JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,"请输入正确编号.","消息",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
