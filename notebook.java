import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class notebook extends JFrame {
    int START = 0, END = 0;
    private UndoManager manager = new UndoManager();
    private JTextArea text = new JTextArea();
    private String book = "新建笔记本";
    private JFileChooser jfc = new JFileChooser();
    private File file;
    private JMenuBar menu;

    //菜单栏
    private JMenu fileBar, editBar, formatBar, viewBar, helpBar;

    //文件菜单 file bar
    private JMenuItem fileBarCreat, fileBarOpen, fileBarSave, fileBarOthersSave, fileBarExit;

    //编辑菜单 edit
    private JMenuItem editBarRevoke, editBarShear, editBarPaste, editBarDelete, editBarCopy;

    //格式菜单
    private JMenuItem formatBarHl, formatBarZtxz, viewBarAboout, helpBarHelp;

    //查找替换菜单
    private JMenuItem formatBarFindReplace;

    private JLabel statusLabel1;
    private JToolBar statusBar;

    //获取时间
    GregorianCalendar time = new GregorianCalendar();
    int hour = time.get(Calendar.HOUR_OF_DAY);
    int min = time.get(Calendar.MINUTE);
    int second = time.get(Calendar.SECOND);


    public class filter extends FileFilter
    {

        @Override
        public boolean accept(File f) {
            String name = f.getName();
            name.toString();
            if(name.endsWith(".txt") || f.isDirectory())
            {
                return true;
            }else
                return false;
        }
        @Override
        public String getDescription() {
            return ".txt";
        }
    }


    //将菜单项 JMenu 添加菜单 JMenuBar
    public JMenu addBar(String name, JMenuBar menu)
    {
        JMenu jMenu = new JMenu(name);
        menu.add(jMenu);
        return jMenu;
    }


    //将菜单项 JMenuItem 添加到菜单 JMenu
    public JMenuItem addItem(String name, JMenu menu)
    {
        JMenuItem jMenuItem = new JMenuItem(name);
        menu.add(jMenuItem);
        return jMenuItem;
    }
    class Clock extends Thread
    { //模拟时钟
         public void run()
         {
            while (true) {
                GregorianCalendar time = new GregorianCalendar();
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int min = time.get(Calendar.MINUTE);
                int second = time.get(Calendar.SECOND);
                statusLabel1.setText("当前时间：" + hour + ":" + min + ":" + second);
                try {
                    Thread.sleep(950);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    notebook note;
    //public void note
    {
        Container container = getContentPane();
        //设置窗口标题
        setTitle(book);
        //设置边界
        setBounds(250,250,500,500);
        //添加菜单 JMenuBar
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        //获取应用程序当前文档的信息
        text.getDocument().addUndoableEditListener(manager);
        text.setFont(new Font("宋体",Font.PLAIN,18));

        //光标颜色
        text.setCaretColor(Color.gray);
        //选中字体的原色
        text.setSelectedTextColor(Color.blue);
        //选中的背景
        text.setSelectionColor(Color.green);
        //是否换行
        text.setLineWrap(true);
        //是否单词边界换行
        text.setWrapStyleWord(true);
        //文本区域与边框的间距，四个参数分别为上左下右
        text.setMargin(new Insets(3,5,3,5));
        //创建一个 JScrollPane , 他将视图组件显示在一个视口中，视口位置可使用一堆滚动条控制
        add(new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        fileBar = this.addBar("文件((F)", menu);
        fileBar.setMnemonic('F');

        editBar = this.addBar("编辑(E)", menu);
        editBar.setMnemonic('E');

        formatBar = this.addBar("格式(O)", menu);
        formatBar.setMnemonic('O');

        viewBar = this.addBar("查看(V)",menu);
        viewBar.setMnemonic('V');

        helpBar = this.addBar("帮助(H)",menu);
        helpBar.setMnemonic('H');

        //文件选项

        //新建文件
        fileBarCreat = this.addItem("新建(N)  Ctrl+N", fileBar);

        fileBarCreat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.setText("");
            }
        });
        //打开选项
        fileBarOpen = this.addItem("打开(O)   Ctrl+O",fileBar);
        fileBarOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    //设置当前文件目录
                    jfc.setCurrentDirectory(new File("."));
                    //过滤文件
                    jfc.setFileFilter(new filter());
                    //全选文件
                    jfc.setAcceptAllFileFilterUsed(false);
                    //弹出一个 "Open File" 文件选择器对话框。
                    jfc.showOpenDialog(null);
                    //获取已经选择的目录
                    file = jfc.getSelectedFile();
                    book = file.getName();
                    setTitle(book);
                    int length = (int) (jfc.getSelectedFile()).length();
                    char[] ch = new char[length];
                    FileReader fr = new FileReader(file);
                    fr.read(ch);
                    book = new String(ch);
                    //获取对象的字段的值，然后转成string类型
                    // Trim()是去两边空格
                    text.setText(book.trim());

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //保存选项
        //1.文件为空，新建一个目录保存
        //2.文件存在，直接保存
        fileBarSave = this.addItem("保存(S)   Ctrl+O",fileBar);
        fileBarSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(file == null)
                {
                    try
                    {
                        jfc = new JFileChooser();
                        jfc.setCurrentDirectory(null);
                        book = JOptionPane.showInputDialog("请输入文件名：") + ".txt";

                        jfc.setSelectedFile(new File(book));
                        jfc.setFileFilter(new filter());
                        //获取当前对象
                        int tem = jfc.showSaveDialog(null);
                        //获取选中的文件对象
                        if(tem == jfc.APPROVE_OPTION)
                        {
                            if(file != null)
                            {
                                file.delete();
                            }
                            file = new File(jfc.getCurrentDirectory(),book);
                            file.createNewFile();
                            FileWriter fw=new FileWriter(file);
                            fw.write(text.getText());
                            fw.close();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(null, ex);
                    }
                }
                else
                {
                    try
                    {
                        FileWriter fw = new FileWriter(file);
                        fw.write(text.getText());
                        fw.close();
                    }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            }
        });

    //另存为选项
       fileBarOthersSave = this.addItem("另存为(A)...", fileBar);
       fileBarOthersSave.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
                   jfc = new JFileChooser();
                   jfc.setCurrentDirectory(new File("."));
                   try
                   {
                       if(file == null)
                       {
                           book = JOptionPane.showInputDialog("请输入文件名：") + ".txt";

                       }
                       else
                       {
                           book = file.getName();

                       }
                       jfc.setSelectedFile(new File(book));
                       jfc.setFileFilter(new filter());
                       int tem = jfc.showSaveDialog(null);
                       if(tem == jfc.APPROVE_OPTION)
                       {
                           if(file != null)
                           {
                               file.delete();
                           }
                           file = new File(jfc.getCurrentDirectory(), book);
                           file.createNewFile();
                           FileWriter fw = new FileWriter(file);
                           fw.write(text.getText());
                           fw.close();
                       }
                   }
                   catch (Exception ex)
                   {
                       JOptionPane.showMessageDialog(null, ex);
                   }
               }
       });
//将默认大小的分隔符添加到工具栏的末尾
fileBar.addSeparator();
//推出保存+提示
        fileBarExit = this.addItem("退出(X)",fileBar);
        fileBarExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int state = JOptionPane.showConfirmDialog(note, "推出前请确保您的文件已保存");
                if(state == JOptionPane.OK_OPTION)
                    System.exit(0);
            }
        });

        //编辑选项
        //撤销选项
        editBarRevoke = this.addItem("撤销(U) Ctrl+Z", editBar);
        editBarRevoke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(manager.canUndo())
                    manager.undo();
            }
        });

        //剪切选项
        editBarShear = this.addItem("剪切(T)  Ctrl+X", editBar);
        editBarShear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.cut();
            }
        });
        //复制选项
        editBarCopy = this.addItem("复制(C)   Ctrl+C", editBar);
        editBarCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.copy();
            }
        });
        //粘贴选项
        editBarPaste = this.addItem("粘贴(P)  Ctrl+V", editBar);
        editBarCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.paste();
            }
        });

        //删除选项 用空格替代从当前选取的开始到结束
        editBarDelete = this.addItem("删除(L) Del", editBar);
        editBarDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.replaceRange("",text.getSelectionStart(), text.getSelectionEnd());
            }
        });

        //自动换行选项
        JCheckBoxMenuItem formatBarHI = new JCheckBoxMenuItem("自动换行(W)", true);
        formatBar.add(formatBarHI);
        formatBarHI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(formatBarHI.getState())
                {
                    text.setLineWrap(true);
                }
                else
                    text.setLineWrap(false);
            }
        });

        //字体选项
        formatBarZtxz = this.addItem("字体选择(F)", formatBar);
        formatBarZtxz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取本地的图形环境
                GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                //字体名称列表框
                JList fontnames = new JList(getFont().getAvailableAttributes());
                //JScrollPane 管理窗口视图，可选的垂直和水平的滚动条以及可选的行列标题视图
                int selection = JOptionPane.showConfirmDialog(null, new JScrollPane(fontnames),
                        "请选择字体", JOptionPane.OK_CANCEL_OPTION);
                Object selectedFont  = fontnames.getSelectedValue();
                if(selection == JOptionPane.OK_OPTION && selectedFont != null)
                {
                    text.setFont(new Font(fontnames.getSelectedValue().toString(), Font.PLAIN, 20));
                }
            }
        });

        //字体颜色设置选项
        formatBarZtxz = this.addItem("颜色(C)", formatBar);
        formatBarZtxz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "文字颜色选择", Color.BLACK);
                text.setForeground(color);

            }
        });

        //替换和查找
        formatBarFindReplace = this.addItem("替换(R) || 查找(F)", formatBar);
        formatBarFindReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog search = new JDialog();
                search.setSize(300, 100);
                search.setLocation(450, 350);
                JLabel labelOne = new JLabel("查找的内容");
                JLabel labelSecond = new JLabel("替换的内容");
                JTextField textFieldOne = new JTextField(5);
                JTextField textFieldSecond = new JTextField(5);
                JButton buttonFind = new JButton("查找下一个");
                JButton buttonChange = new JButton("替换");
                JPanel panel = new JPanel(new GridLayout(2, 3));
                panel.add(labelOne);
                panel.add(textFieldOne);
                panel.add(buttonFind);

                panel.add(labelSecond);
                panel.add(textFieldSecond);
                panel.add(buttonChange);

                search.add(panel);
                search.setVisible(true);

                //为查找添加监听器
                buttonFind.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //查找字符
                        String findText = textFieldOne.getText();
                        //当前文本框的内容
                        String textArea = text.getText();

                        START = textArea.indexOf(findText,END);
                        END = START + findText.length();
                        if(START == -1)//表示没有找到
                        {
                            JOptionPane.showMessageDialog(null, "没找到" +
                                    findText, "记事本", JOptionPane.WARNING_MESSAGE);
                            text.select(START, END);
                        }
                        else
                            text.select(START, END);

                    }
                });

                //为替换绑定监听器时间
                buttonChange.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //替换字符
                        String changeText = textFieldSecond.getText();
                        if(text.getSelectionStart() != text.getSelectionEnd())
                        {
                            text.replaceRange(changeText, text.getSelectionStart(), text.getSelectionEnd());
                        }

                    }
                });
            }
        });
        //关于选项
        viewBarAboout = this.addItem("关于记事本(About)", viewBar);
        viewBarAboout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "记事本\n 开发语言: JAVA\n 开发者: 【张宇恒】\n 联系方式：yanzhileizyh.@gmail.com",
                        "关于", JOptionPane.PLAIN_MESSAGE);

            }
        });

        //帮助选项
        helpBarHelp = this.addItem("帮助选项(H)", helpBar);
        helpBarHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "if you have any question please call Yuheng Zhang(QQ: 2319355427) for help",
                        "帮助", JOptionPane.PLAIN_MESSAGE);
            }
        });


        //向容器中添加工具栏
        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        //创建和添加状态栏
        container.add(toolBar, BorderLayout.NORTH);
//        toolBar.add(fileBar);
//        toolBar.add(editBar);
//        toolBar.add(formatBar);
//        toolBar.add(helpBar);
//        toolBar.add(viewBar);

        statusBar = new JToolBar();
        statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusLabel1 = new JLabel("当前时间：" + hour+ ":"+ min + ":" + second);
        statusBar.add(statusLabel1);
        statusBar.addSeparator();
        container.add(statusBar, BorderLayout.SOUTH);
        statusBar.setVisible(true);

        Clock clock = new Clock();
        clock.start();
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public static void main(String args[])
    {
        notebook book = new notebook();
    }








}
