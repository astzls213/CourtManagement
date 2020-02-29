import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;

public class ReportFrame extends JFrame {
    //false代表因设备坏了
    //true代表修好了
    public ReportFrame(boolean flag,String bossName){
        setLocationRelativeTo(null);
        setTitle("报修窗口");
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();
        p1.setLayout(new GridLayout(2,2));
        JLabel id=new JLabel("编号：",SwingConstants.RIGHT);
        JLabel info=new JLabel("详细信息：",SwingConstants.RIGHT);

        JTextField ID=new JTextField(8);
        JTextField Info=new JTextField();
        Info.setSize(8,5);

        p1.add(id);
        p1.add(ID);
        p1.add(info);
        p1.add(Info);
        add(p1, BorderLayout.NORTH);

        JButton ok=new JButton("ok");
        ok.addActionListener(event->{
            try{
                //读取球场信息
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream(bossName+"的球场.txt"));
                CourtMsg cm=(CourtMsg)ois.readObject();
                ois.close();
                //找到指定球场
                int find=-1;
                for(int i=0;i<cm.numbers;i++){
                    if(cm.court[i][0].equals(ID.getText()))
                        find=i;
                }
                if(find==-1)
                    throw new IOException();
                //修改信息
                cm.court[find][9]=Info.getText();
                cm.court[find][1]=flag;
                //保存修改
                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(bossName+"的球场.txt"));
                oos.writeObject(cm);
                oos.close();
                dispose();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,"请填写正确编号！","消息",JOptionPane.ERROR_MESSAGE);
            }

        });
        p2.add(ok);
        add(p2,BorderLayout.SOUTH);
        pack();
    }
}
