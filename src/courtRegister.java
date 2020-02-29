import javax.swing.*;
import java.awt.*;
import java.io.*;

public class courtRegister extends JFrame {
    private int loop=0;
    private CourtMsg courtMsg;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private JTable tmp;
    public courtRegister(int n,String boss){
        setLocationRelativeTo(null);
        //取出对应信息
        try{
            ois=new ObjectInputStream(new FileInputStream(boss+"的球场.txt"));
            courtMsg=(CourtMsg)ois.readObject();
            ois.close();
        }
        catch(IOException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null,"未知错误！","消息",JOptionPane.ERROR_MESSAGE);
        }
        //设定循环次数
        loop=n;
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();
        JPanel p3=new JPanel();

        JLabel l1=new JLabel("代号：",SwingConstants.RIGHT);
        JLabel l2=new JLabel("单价(每小时)：",SwingConstants.RIGHT);

        JTextField id=new JTextField(8);
        JTextField price=new JTextField(8);

        JButton b1=new JButton("确定");

        p1.add(l1);
        p1.add(id);

        p2.add(l2);
        p2.add(price);

        p3.add(b1);

        add(p1, BorderLayout.NORTH);
        add(p2,BorderLayout.CENTER);
        add(p3,BorderLayout.SOUTH);
        b1.addActionListener(event->{
            //写入信息
            courtMsg.court[courtMsg.numbers][0]=id.getText();//代号
            courtMsg.court[courtMsg.numbers][1]=true;//设施完好？
            courtMsg.court[courtMsg.numbers][2]=true;//空闲？
            courtMsg.court[courtMsg.numbers][3]="";//占用者名字
            courtMsg.court[courtMsg.numbers][4]=null;//开始时间
            courtMsg.court[courtMsg.numbers][5]=null;//结束时间
            courtMsg.court[courtMsg.numbers][6]=0.0;//累计收入
            courtMsg.court[courtMsg.numbers][7]=price.getText();//单价
            courtMsg.court[courtMsg.numbers][8]="";//管理人姓名
            courtMsg.court[courtMsg.numbers][9]="";//其他信息
            courtMsg.numbers++;

            //清空文本
            id.setText("");
            price.setText("");
            //循环次数-1
            loop--;
            //判断循环是否结束
            if(loop==0)
            {
                //保存信息
                try{
                    oos=new ObjectOutputStream(new FileOutputStream(boss+"的球场.txt"));
                    oos.writeObject(courtMsg);
                    oos.close();
                }
                catch(IOException e){
                    JOptionPane.showMessageDialog(null,"未知意外！","message",JOptionPane.ERROR_MESSAGE);
                }
                //关闭窗口
                dispose();
            }
            //成功提示
            JOptionPane.showMessageDialog(null,"注册成功！","message",JOptionPane.INFORMATION_MESSAGE);
        });

        pack();
    }
}
