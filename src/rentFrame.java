import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.IllegalFormatCodePointException;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipException;

public class rentFrame extends JFrame {
    private LocalDateTime current;
    public rentFrame(String boss){
        current=LocalDateTime.now();
        setLocationRelativeTo(null);
        pack();
        setTitle("租借窗口");
        JPanel upPanel=new JPanel();
        JPanel centerPanel=new JPanel();
        JPanel buttonPanel=new JPanel();
        GridLayout gl=new GridLayout(3,5);
        gl.setVgap(1);

        upPanel.setLayout(new GridLayout(4,2));
        centerPanel.setLayout(gl);

        JLabel id=new JLabel("代号：",SwingConstants.RIGHT);
        JLabel occupier=new JLabel("租赁人：",SwingConstants.RIGHT);
        JLabel price=new JLabel("应收：",SwingConstants.RIGHT);
        JLabel manager=new JLabel("管理人：",SwingConstants.RIGHT);
        JLabel date=new JLabel("日期：",SwingConstants.RIGHT);
        JLabel month=new JLabel("月",SwingConstants.CENTER);
        JLabel day=new JLabel("日",SwingConstants.CENTER);
        JLabel from=new JLabel("从",SwingConstants.RIGHT);
        JLabel to=new JLabel("到",SwingConstants.RIGHT);
        JLabel separator1=new JLabel(":",SwingConstants.CENTER);
        JLabel separator2=new JLabel(":",SwingConstants.CENTER);
        JLabel hide=new JLabel("",SwingConstants.RIGHT);

        JTextField idText=new JTextField(8);
        JTextField occupierText=new JTextField(8);
        JTextField priceText=new JTextField(8);
        JTextField managerText=new JTextField(8);
        JTextField monthText=new JTextField(2);
        JTextField dayText=new JTextField(2);
        JTextField start_hours=new JTextField(2);
        JTextField start_minute=new JTextField(2);
        JTextField end_hours=new JTextField(2);
        JTextField end_minute=new JTextField(2);

        upPanel.add(id);
        upPanel.add(idText);
        upPanel.add(occupier);
        upPanel.add(occupierText);
        upPanel.add(price);
        upPanel.add(priceText);
        upPanel.add(manager);
        upPanel.add(managerText);
        add(upPanel,BorderLayout.NORTH);

        centerPanel.add(date);
        centerPanel.add(monthText);
        centerPanel.add(month);
        centerPanel.add(dayText);
        centerPanel.add(day);
        centerPanel.add(from);
        centerPanel.add(start_hours);
        centerPanel.add(separator1);
        centerPanel.add(start_minute);
        centerPanel.add(hide);
        centerPanel.add(to);
        centerPanel.add(end_hours);
        centerPanel.add(separator2);
        centerPanel.add(end_minute);

        add(centerPanel,BorderLayout.CENTER);

        JButton ok=new JButton("确定");
        buttonPanel.add(ok);
        add(buttonPanel,BorderLayout.SOUTH);
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
                    if(cm.court[i][0].equals(idText.getText()))
                        find=i;
                }
                if(find==-1)
                    throw new IOException();
                //修改信息
                cm.court[find][3]=occupierText.getText();
                cm.court[find][8]=managerText.getText();
                cm.court[find][2]=false;
                //转换类型
                int yue=Integer.parseInt(monthText.getText());
                int tian=Integer.parseInt(dayText.getText());
                int sh=Integer.parseInt(start_hours.getText());
                int sm=Integer.parseInt(start_minute.getText());
                int eh=Integer.parseInt(end_hours.getText());
                int em=Integer.parseInt(end_minute.getText());

                cm.court[find][4]=LocalDateTime.of(current.getYear(),yue,tian,sh,sm,0);
                cm.court[find][5]=LocalDateTime.of(current.getYear(),yue,tian,eh,em,0);
                //判断是否合理
                LocalDateTime judge=((LocalDateTime)cm.court[find][4]).plusMinutes(2);
                if(((LocalDateTime)cm.court[find][5]).isBefore(judge)){
                    throw new NumberFormatException();
                }
                if(((LocalDateTime)cm.court[find][4]).isBefore(current)){

                    throw new ZipException();
                }
                //保存修改
                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(boss+"的球场.txt"));
                oos.writeObject(cm);
                oos.close();
                //创建警报线程
                final long diff= Duration.between(current,(LocalDateTime) cm.court[find][5]).toSeconds();
                int finalFind = find;
                Runnable r=()->{
                    try {
                        ObjectInputStream tmpOIS=new ObjectInputStream(new FileInputStream(boss+"的球场.txt"));
                        CourtMsg tmp=(CourtMsg)tmpOIS.readObject();
                        tmpOIS.close();
                        //diff-120是指两分钟前显示警告
                        for(long i=0;i<diff-120&&!(Boolean)tmp.court[finalFind][2];i++){
                            try {
                                Thread.sleep(1000);
                                tmpOIS=new ObjectInputStream(new FileInputStream(boss+"的球场.txt"));
                                tmp=(CourtMsg)tmpOIS.readObject();
                                tmpOIS.close();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(!(Boolean)tmp.court[finalFind][2])
                            JOptionPane.showMessageDialog(null,"编号为"+ tmp.court[finalFind][0]+"的球场就要当时间了！","消息", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                };
                Thread thread=new Thread(r);
                thread.start();
                dispose();
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"租场时间必须大于1小时！","消息",JOptionPane.ERROR_MESSAGE);
            }
            catch(ZipException e){
                JOptionPane.showMessageDialog(null,"时间有误！请检查您的时间。","消息",JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,"未知错误！","消息",JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
