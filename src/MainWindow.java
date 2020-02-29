import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainWindow extends JFrame {
    private JMenuBar menuBar;
    private JMenu controlMenu;
    private JMenuItem incomeItem;
    private JMenu registerMenu;
    private JMenuItem rentItem;
    private JMenuItem retrieveItem;
    private JMenu reportMenu;
    private JMenuItem stuffItem;
    private JMenuItem courtItem;
    public JTable table;

    private JScrollPane jScrollPane;
    private JPanel center;
    private String userName;
    private boolean stuff;

    private ObjectInputStream ois;

    private static final int DEFAULT_WIDTH=800;
    private static final int DEFAULT_HEIGHT=600;

    //stuff 是判断是负责人(true)进来还是管理人员(false)进来
    public MainWindow(String userName,boolean stuff,String bossName){
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        //加载球场信息
        try{
            center=new JPanel();
            if(stuff)
                ois=new ObjectInputStream(new FileInputStream(userName+"的球场.txt"));
            else
                ois=new ObjectInputStream(new FileInputStream(bossName+"的球场.txt"));
            CourtMsg courtMsg=(CourtMsg)ois.readObject();
            String[] columnNames={"场地编号","设施状态","占用情况","占用者","开始时间","结束时间","累计利润","单价","管理人","其他信息"};
            table=new JTable(courtMsg.court,columnNames);
            jScrollPane=new JScrollPane(table);
            jScrollPane.setPreferredSize(new Dimension(800,400));
            center.add(jScrollPane);
            add(center);
            JTableHeader jTableHeader=table.getTableHeader();
            jTableHeader.setFont(new Font(null,Font.PLAIN,14));
            table.setRowHeight(60);
            table.setGridColor(Color.GRAY);
            //为管理人员过滤掉不可租赁掉场地
            if(!stuff){
                TableRowSorter<TableModel> sorter=new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.andFilter(Arrays.asList(
                        RowFilter.regexFilter("^true$",1),
                        RowFilter.regexFilter("^true$",2)
                )));
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //刷新功能
        JPanel buttonPanel=new JPanel();
        JButton flash=new JButton("刷新");
        buttonPanel.add(flash);

        flash.addActionListener(event->{
            try{
                if(stuff){
                    ois=new ObjectInputStream(new FileInputStream(userName +"的球场.txt"));
                }
                else{
                    ois=new ObjectInputStream(new FileInputStream(bossName +"的球场.txt"));
                }
                remove(center);
                center=new JPanel();

                CourtMsg courtMsg=(CourtMsg)ois.readObject();
                String[] columnNames={"场地编号","设施状态","占用情况","占用者","开始时间","结束时间","累计利润","单价","管理人","其他信息"};
                table=new JTable(courtMsg.court,columnNames);
                jScrollPane=new JScrollPane(table);
                jScrollPane.setPreferredSize(new Dimension(800,400));
                center.add(jScrollPane);
                add(center);
                JTableHeader jTableHeader=table.getTableHeader();
                jTableHeader.setFont(new Font(null,Font.PLAIN,14));
                table.setRowHeight(60);
                table.setGridColor(Color.GRAY);
                center.updateUI();
                center.repaint();
                ois.close();
                //为管理人员过滤掉不可租赁掉场地
                if(!stuff){
                    TableRowSorter<TableModel> sorter=new TableRowSorter<>(table.getModel());
                    table.setRowSorter(sorter);
                    sorter.setRowFilter(RowFilter.andFilter(Arrays.asList(
                            RowFilter.regexFilter("^true$",1),
                            RowFilter.regexFilter("^true$",2)
                    )));
                }
            }
            catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,"刷新失败！","消息",JOptionPane.ERROR_MESSAGE);
            }
            invalidate();
        });
        add(buttonPanel,BorderLayout.SOUTH);


        //首次登陆为老板创建球场管理信息。
        if(stuff){
            File courtInfo=new File(userName+"的球场.txt");
            try{
                //create this file.
                boolean first=courtInfo.createNewFile();
                //初始化球场信息，设置球场最大数量为10
                if(first){
                    ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(courtInfo));
                    oos.writeObject(new CourtMsg(0,new Object[10][10]));
                    oos.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        //绘制菜单栏按钮。
        menuBar=new JMenuBar();
        setJMenuBar(menuBar);
        controlMenu=new JMenu("Control");
        menuBar.add(controlMenu);

        if(stuff){
            setTitle("羽毛球场管理系统 负责人:"+" "+userName);
            incomeItem=new JMenuItem("Income");
            controlMenu.add(incomeItem);
            controlMenu.addSeparator();
            registerMenu=new JMenu("Register");
            controlMenu.add(registerMenu);
            stuffItem=new JMenuItem("Stuffs");
            courtItem=new JMenuItem("Courts");
            registerMenu.add(courtItem);
            registerMenu.add(stuffItem);




            courtItem.addActionListener(event->{
                try{
                    ObjectInputStream ois=new ObjectInputStream(new FileInputStream(userName+"的球场.txt"));
                    CourtMsg cm=(CourtMsg)ois.readObject();
                    Register reg=new Register(cm.numbers,true,userName,this);
                    reg.setVisible(true);
                    ois.close();
                }
                catch(ClassNotFoundException | IOException e){
                    JOptionPane.showMessageDialog(null,"未知错误！","消息",JOptionPane.ERROR_MESSAGE);
                }
            });

            stuffItem.addActionListener(event->{
                try{
                    ObjectInputStream ois=new ObjectInputStream(new FileInputStream("管理人员信息.txt"));
                    Set<Management> mi=(Set<Management>) ois.readObject();
                    Register reg=new Register(mi.size(),false,userName,this);
                    reg.setVisible(true);
                    ois.close();
                }
                catch(ClassNotFoundException | IOException e){
                    JOptionPane.showMessageDialog(null,"未知错误！","消息",JOptionPane.ERROR_MESSAGE);
                }
            });
            incomeItem.addActionListener(event->{
                try {
                    ObjectInputStream ois=new ObjectInputStream(new FileInputStream(userName+"的球场.txt"));
                    CourtMsg cm=(CourtMsg)ois.readObject();
                    double total=0;
                    for(Object[] Obj:cm.court){
                        if(Obj[6]!=null)
                            total+=Double.parseDouble(Obj[6].toString());
                    }
                    JOptionPane.showMessageDialog(null,"历史收益："+" "+total+"元。","消息",JOptionPane.INFORMATION_MESSAGE);
                }
                catch(NullPointerException e){
                    JOptionPane.showMessageDialog(null,"没有数据！","消息",JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException | ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null,"数据损坏！","消息",JOptionPane.ERROR_MESSAGE);
                }

            });
        }
        //Manager Mode
        else{
            setTitle("羽毛球场管理系统 管理人:"+" "+userName);
            //添加租借，收回，报修功能
            rentItem=new JMenuItem("Rent");
            retrieveItem=new JMenuItem("Retrieve");
            reportMenu=new JMenu("Report");
            controlMenu.add(rentItem);
            controlMenu.add(retrieveItem);
            controlMenu.addSeparator();
            controlMenu.add(reportMenu);
            JMenuItem damageItem=new JMenuItem("Need Fix");
            JMenuItem fixItem=new JMenuItem("Fixed");
            reportMenu.add(damageItem);
            reportMenu.add(fixItem);

            rentItem.addActionListener(event->{
                rentFrame rf=new rentFrame(bossName);
                rf.setVisible(true);
            });
            retrieveItem.addActionListener(event->{
                RetrieveFrame rf=new RetrieveFrame(bossName);
                rf.setVisible(true);
            });
            damageItem.addActionListener(event->{
                ReportFrame rf=new ReportFrame(false,bossName);
                rf.setVisible(true);
            });
            fixItem.addActionListener(event->{
                ReportFrame rf=new ReportFrame(true,bossName);
                rf.setVisible(true);
            });
        }
        pack();
    }
}
