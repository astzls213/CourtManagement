import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Log extends JFrame {
    private static final int DEFAULT_WIDTH=800;
    private static final int DEFAULT_HEIGHT=600;
    //单选按钮
    private ButtonGroup group;
    private JRadioButton Management;
    private JRadioButton Boss;
    private Principle tmp;
    private Set<Principle> pi;
    private Set<Management> mi;

    public MainWindow mainWindow;
    public Log(){
        setResizable(true);
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        JTextField name=new JTextField();
        JTextField userField=new JTextField();
        JPasswordField passwordField=new JPasswordField();
        setTitle("登陆界面");

        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(2,2));


        panel.add(new JLabel("账号: ",SwingConstants.RIGHT));
        panel.add(userField);
        panel.add(new JLabel("密码: ",SwingConstants.RIGHT));
        panel.add(passwordField);

        add(panel,BorderLayout.NORTH);

        JPanel middlePanel=new JPanel();
        group=new ButtonGroup();
        Boss=new JRadioButton("负责人",true);
        Management=new JRadioButton("管理人员",false);
        group.add(Boss);
        group.add(Management);

        middlePanel.add(Boss);
        middlePanel.add(Management);

        add(middlePanel,BorderLayout.CENTER);

        JPanel buttonPanel=new JPanel();
        JButton complete=new JButton("注册");
        buttonPanel.add(complete);
        complete.setVisible(false);

        JButton register=new JButton("立即注册");
        buttonPanel.add(register);
        JButton log_in=new JButton("登陆");
        buttonPanel.add(log_in);

        add(buttonPanel,BorderLayout.SOUTH);

        register.addActionListener(event->{
            middlePanel.setVisible(false);
            name.setVisible(true);
            register.setVisible(false);
            log_in.setVisible(false);
            complete.setVisible(true);
            userField.setText("");
            passwordField.setText("");
            setTitle("注册界面");
        });

        log_in.addActionListener(event->{
            boolean flag=false;
            String bossName="";
            try {
                ObjectInputStream ois;
                Principle loginPerson;
                if(Boss.isSelected())
                {
                    ois=new ObjectInputStream(new FileInputStream("负责人信息.txt"));
                    pi=(Set<Principle>)ois.readObject();
                    loginPerson=new Principle(userField.getText(),passwordField.getPassword());
                    for(Principle p:pi){
                        if(loginPerson.equals(p)){
                            JOptionPane.showMessageDialog(null,"登陆成功！","消息",JOptionPane.INFORMATION_MESSAGE);
                            flag=true;
                        }
                    }
                }
                else
                {
                    ois=new ObjectInputStream(new FileInputStream("管理人员信息.txt"));
                    mi=(Set<Management>)ois.readObject();
                    Management loginManager=new Management(userField.getText(),passwordField.getPassword(),"");
                    for(Management m:mi){
                        if(loginManager.equals(m)){
                            bossName=m.getBoss();
                            JOptionPane.showMessageDialog(null,"登陆成功！","消息",JOptionPane.INFORMATION_MESSAGE);
                            flag=true;
                        }
                    }
                }
                ois.close();
                if(!flag)
                    JOptionPane.showMessageDialog(null,"登陆失败！","消息",JOptionPane.ERROR_MESSAGE);
                else{
                    this.setVisible(false);
                    mainWindow=new MainWindow(userField.getText(),Boss.isSelected(),bossName);
                    mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    mainWindow.setVisible(true);
                }
            }
            catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,"登陆失败！","消息",JOptionPane.ERROR_MESSAGE);
            }

        });

        complete.addActionListener(event->{
            try {
                //读取之前信息
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream("负责人信息.txt"));
                pi= (Set<Principle>) ois.readObject();
                //更新信息
                tmp=new Principle(userField.getText(),passwordField.getPassword());
                pi.add(tmp);
                ois.close();
                //写入
                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("负责人信息.txt"));
                oos.writeObject(pi);
                oos.close();
            }
            catch (ClassNotFoundException| IOException e) {
                ObjectOutputStream oos= null;
                try {
                    oos = new ObjectOutputStream(new FileOutputStream("负责人信息.txt"));
                    pi=new HashSet<>();
                    tmp=new Principle(userField.getText(),passwordField.getPassword());
                    pi.add(tmp);
                    oos.writeObject(pi);
                    oos.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            finally {
                name.setText("");
                userField.setText("");
                passwordField.setText("");
                middlePanel.setVisible(true);
                name.setVisible(false);
                complete.setVisible(false);
                register.setVisible(true);
                log_in.setVisible(true);
                setTitle("登陆界面");
            }
        });

        pack();
    }

}
