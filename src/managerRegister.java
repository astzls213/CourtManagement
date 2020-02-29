import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class managerRegister extends JFrame {
    private int loop=0;
    private Set<Management> managerInfo;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public managerRegister(int n,String boss){
        //设定循环次数
        loop=n;
        JPanel p1=new JPanel();
        JPanel p2=new JPanel();
        JPanel p3=new JPanel();

        JLabel l1=new JLabel("账号：",SwingConstants.RIGHT);
        JLabel l2=new JLabel("密码：",SwingConstants.RIGHT);

        JTextField id=new JTextField(8);
        JPasswordField passwordField=new JPasswordField(8);

        JButton b1=new JButton("确定");

        p1.add(l1);
        p1.add(id);

        p2.add(l2);
        p2.add(passwordField);

        p3.add(b1);

        add(p1, BorderLayout.NORTH);
        add(p2,BorderLayout.CENTER);
        add(p3,BorderLayout.SOUTH);
        b1.addActionListener(event->{
            try{
                //读取信息
                ois=new ObjectInputStream(new FileInputStream("管理人员信息.txt"));
                managerInfo=(Set<Management>) ois.readObject();
                //写入信息
                managerInfo.add(new Management(id.getText(),passwordField.getPassword(),boss));
                //清空文本
                id.setText("");
                passwordField.setText("");
                //循环次数-1
                loop--;
                //判断循环是否结束
                if(loop==0)
                {
                    //保存信息
                    try{
                        oos=new ObjectOutputStream(new FileOutputStream("管理人员信息.txt"));
                        oos.writeObject(managerInfo);
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
            }
            catch (ClassNotFoundException| IOException e) {
                ObjectOutputStream oos= null;
                try {
                    oos = new ObjectOutputStream(new FileOutputStream("管理人员信息.txt"));
                    managerInfo=new HashSet<>();
                    managerInfo.add(new Management(id.getText(), passwordField.getPassword(),boss));
                    oos.writeObject(managerInfo);
                    oos.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        pack();
    }
}
