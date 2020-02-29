import javax.swing.*;
import java.awt.*;


public class Register extends JFrame {
    //num用来表明已注册的个数,true表示球场，false表示管理人员
    public Register(int num,boolean flag,String boss,MainWindow parent) {
        setLocationRelativeTo(parent);

        setTitle("申请表单");
        JPanel p1=new JPanel();

        JPanel p3=new JPanel();

        p1.setLayout(new GridLayout(1,2));

        JLabel prompt=new JLabel("新增：",SwingConstants.RIGHT);
        JTextField addition=new JTextField(6);
        p1.add(prompt);
        p1.add(addition);

        JButton ok=new JButton("确定");
        p3.add(ok);
        add(p1,BorderLayout.NORTH);
        add(p3,BorderLayout.SOUTH);

        pack();
        setSize(250,150);

        ok.addActionListener(event->{
            int n;
            try{
                n=Integer.parseInt(addition.getText());
                //判断有无溢出人数
                if(num+n>10){
                    JOptionPane.showMessageDialog(null,"数目超出！\n请重新确认您的数目！","消息",JOptionPane.ERROR_MESSAGE);
                    addition.setText("");
                }
                else{
                    //球场
                    if(flag){
                        courtRegister cr=new courtRegister(n,boss);
                        cr.setVisible(true);
                    }
                    //管理人员
                    else{
                        managerRegister mr=new managerRegister(n,boss);
                        mr.setVisible(true);
                    }
                    //关闭窗口
                    dispose();
                }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"请输入数字！\n请重新确认您的数目！","消息",JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
