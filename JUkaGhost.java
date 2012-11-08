/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagaka.ghost;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import jukagaka.*;
import jukagaka.shell.*;
import jukagaka.shell.cyaushell.CyauShell;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class JUkaGhost extends JUkaComponent implements Serializable
{
  // Shell Manipulate | Shell 操控
    /**
     * <p>主要 Shell</p>
     * <p>主要 Shell 是指在主调模块未指定具体 Shell 情况下供给的缺省 Shell,
     * 它通常指向 assignedShell[0].<br>
     * 虽然在此平台上没有定义, 但 masterShell 一般对应于传统的"主人格"</p>
     */
    protected JUkaShell masterShell;

    /**
     * <p>从属 Shell</p>
     * <p>它通常指向 assignedShell[1].<br>
     * 虽然在此平台上没有定义, 但 slaveShell 一般对应于传统的"使魔"</p>
     */
    protected JUkaShell slaveShell;


	 transient private static Object argghost;


    protected GhostOperatingSystem mainGhost;
    /**
     * <p>链接的 Shell 的记录表</p>
     * <p>注意并非必需填充这个数据域(比如只有一个 Shell 时), 这种情况下应该重写
     * getAssignedShell() 以正确地返回需求的 Shell 对象.</p>
     */
    protected ArrayList<JUkaShell> assignedShell = null;

    /**
     * <p>根据指定的序号获取该 Ghost 控制的 Shell </p>
     */
    public JUkaShell getAssignedShell(int argShellID)
    {
        if (argShellID >= this.assignedShell.size())
        {
            System.err.println("Error: JUkaGhost.getAssignedShell(): 参数 argShellID 上溢: " + argShellID);
            return(null);
        }
        return(this.assignedShell.get(argShellID));

        // 在使用 master/slave 模式(不使用 assignedShell 数组)时, 应该按照传入参数
        // 返回 masterShell 或 slaveShell, 参考代码段如下:
        //if (argShellID == 0)
            //return(this.masterShell);
        //if (argShellID == 1)
            //return(this.slaveShell);

        // 在只有单个 shell(不使用 assignedShell 数组)时, 应总是返回 masterShell
        // 参考代码如下:
        //return(this.masterShell);
    }

  // Install/Uninstall | 安装/卸载
    // 请按照要求重写这些方法.
    /**
     * <p>(模板)安装 Ghost 组件</p>
     * <p>Ghost 在可以被使用之前, 必须向 JUkaStage 通知其存在<br>
     * (!) 此类的扩展者应<b>复制</b>这个函数并在 TODO 处填充自撰的代码,
     *     JUkaGhost.java 中原有的部分不能被删减. 否则可能导致运行失败.<br>
     * </p>
     * @return <ul>返回值, 大于等于0均表示执行成功, 反之表示失败
     *  <li>0=正常
     *  <li>1=该组件已安装
     *  <li>-1=(未定义错误)
     *  <li>-2=运行栈上溢
     *  <li>-3=读写错误
     * </ul>
     */
    public static int install()
    {
        // TODO 在此处加入安装前的代码
        // 比如 检查依赖/冲突关系, EULA, 使用说明, 确认对话框, 建立用户数据 等等.
        return(JUkaGhostCtrl.installComponent());
    }

    /**
     * <p>(模板)卸载 Ghost 组件</p>
     * <p>要不再加载某个 Ghost, 必须向 JUkaStage 通知注销<br>
     * (!) 此类的扩展者应<b>复制</b>这个函数并在 TODO 处填充自撰的代码,
     *     JUkaGhost.java 中原有的部分不能被删减. 否则可能导致运行失败.<br>
     * </p>
     * @return <ul>返回值, 大于等于0均表示执行成功, 反之表示失败
     *  <li>0=正常
     *  <li>1=该组件不在列表中
     *  <li>-1=(未定义错误)
     *  <li>-2=运行栈上溢
     *  <li>-3=读写错误
     * </ul>
     */
    public static int uninstall()
    {
        // TODO 在此处加入卸载前的代码
        // 比如 确认对话框, 清理用户数据, 级联卸载等等.
        return(JUkaGhostCtrl.uninstallComponent());
    }

  // Trigger | 触发器
    /**
     * <p>此方法不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>(*) 任何扩展此类的子类都必须以此方法签名重写该方法<br>
     * 子类的该方法会在 JUkagaka 启动时被调用, 以提供机会让 Ghost 完成初始化工作</p>
     */
    protected static void onLoad(String ghostargFile){

            try {
            JUkaGhost.argghost = JUkaGhost.OSReading(ghostargFile);
        } catch (IOException ex) {
            Logger.getLogger(JUkaGhost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JUkaGhost.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    /**
     * <p>此方法不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>(*) 任何扩展此类的子类都必须以此方法签名重写该方法<br>
     * 子类的该方法会在所有 JUkaComponentCtrl 初始化完成后被调用, 以引导 Ghost 开始工作</p>
     */
    protected static void onStart()
    {
        return;
    }

  // Speak & Marco System | 宏解析
    //   ∧
    //  /｜\  TODO: 工事中...
    // /_＾_\

    ///**
     //* <p>指示 Shell 作出通常说话操作</p>
     //* <p>内建宏解析器.(待续)
     //* </p>
     //* @return 最终解析的说话内容
     //*/
    //public String speak(String argSpeech, JUkaShell argShell)


    protected void createGhost(JUkaGhost os){
        docreateGhost(this);
       

	}
    
    private JUkaGhost docreateGhost(JUkaGhost os){
        os.mainGhost=new GhostOperatingSystem();
        os.mainGhost.initalizeOS();
        
		return os;
    }

	protected void initalizeGhost() throws IOException, ClassNotFoundException{
		
            JUkaGhost.doinitalizeGhost(this);
	}
        
    private static void doinitalizeGhost(JUkaGhost argGhost) throws IOException, ClassNotFoundException {
        argGhost=(JUkaGhost)argghost;
    }

  // Other | 杂项
    

	//存储Ghost人格以及部分Shell,进行中


    public void OSStorage(String ghostargFile){
        try {
            JUkaGhost.doOSStorage(this, ghostargFile);
        } catch (IOException ex) {
           System.err.println("error in 196");
        }
    }

    public static void doOSStorage(JUkaGhost argGhost, String ghostargFile) throws IOException {

        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(ghostargFile));
        output.writeObject(argGhost);
        output.close();

    }
        
        
        
    public static Object OSReading(String ghostargFile) throws IOException, ClassNotFoundException{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(ghostargFile));
          Object initghost=input.readObject();
          input.close();
          return initghost;
        }

/*
 * 这个类为茶兔speak类的第一版，修改自第一个CyauGhost,但是其中加入了宏解释功能
 * 此类之后较适合加入于GhostOperatingSystem类，当然加入Ghost中也能运行，此类只能
 * 作为测试功能供你们吐槽的作用。
*/
//public class Speak extends JPanel {
//    private int lth = GetTucaoLength();
//    private JTextArea area = new JTextArea();
//    private String[] tucaoText = GetTucaoText();
//    private int time;
//    
//    int delay = 1500; //milliseconds
//    final int seconddelay = 4000;//第二延时器
//    Timer timer;
//
//    public Speak() throws FileNotFoundException{
//        initalizeSpeak();
//    }
//
//    protected void initalizeSpeak() {
//
//        int delay = 6000; //milliseconds
//        final int seconddelay = 4000;//第二延时器
//
//        timer = new Timer(delay, new Speak.timelistener());
//
//
//    }
//
//        class timelistener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                BalloonWin testBall = masterShell.createBalloon(TOOL_TIP_TEXT_KEY, null, null);
//                time = (int) (Math.random() * lth);            //产生随机内容
//                SpeakCtrl(testBall);
//                area.setLineWrap(true);
//                area.setEditable(false);
//                area.setWrapStyleWord(true);
//                testBall.add(area, BorderLayout.CENTER);
//                testBall.setVisible(true);
//                testBall.setDragable(true);
//
//                ActionListener taskPerformer1 = new ActionListener() {
//
//                    @Override////第二计时器
//                    public void actionPerformed(ActionEvent e) {
//                        setVisible(false);
//                        //   shell.destroyBalloon(newballoon);
//                    }
//                };
//
//                new Timer(seconddelay, taskPerformer1).start();
//            }
//        }
//
//        //
//        public void CheckTucaoFile(File file) {//检查Tucao.txt是否存在
//
//            if (!file.exists()) {//检查Tucao.txt是否存在
//                System.out.println("读取吐槽文件失败");
//                System.exit(0);
//            }
//        }
//
//        public int GetTucaoLength() throws FileNotFoundException {//获取Tucao.txt的行数
//
//            int length = 0;
//            File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");
//            CheckTucaoFile(speakfile);
//            try (Scanner input = new Scanner(speakfile)) {
//                while (input.hasNextLine()) {
//                    length++;
//                    input.nextLine();//将input扫描器移到下一行
//                }
//            }
//
//
//            return length;
//        }
//
//        public String[] GetTucaoText() {//将Tucao.txt中的文本读取并存到一个字符串数组中
//
//
//            String username = null;//用户名
//
//            //username = log.getUsername();//获取用户名
//
//            File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");//读取文本
//            CheckTucaoFile(speakfile);//检查文本是否存在
//            String[] tucaotext = new String[lth];//创建文本行数长度的字符串数组
//            Scanner input = null;//初始化扫描器
//            try {
//                input = new Scanner(speakfile);
//            } catch (FileNotFoundException ex) {
//            }
//            int i = 0;//计算tucaotext字符串数组下标
//            while (input.hasNextLine()) {//是否存在下一行
//                String tempstring = input.nextLine();//获取Tucao.txt中的一行字符串
//                tucaotext[i] = tempstring;//将处理后的字符串存到tucaotext字符串数组中
//                i++;
//            }
//            tucaoText = tucaotext;
//            return tucaotext;
//        }
////吐槽面板开关
//
//        public void setTucao(boolean word) {
//            if (word == true) {
//                timer.start();
//            } else {
//                timer.stop();
//            }
//        }
//
//        public int SpeakCtrl(BalloonWin testBall) {
//            String test = tucaoText[time].trim();
//            StringBuilder demo1 = null;
//            StringBuilder demo2 = null;
//            int i = 0;
//            while (i < test.length()) {
//                char a = test.charAt(i);
//
//                if (a == '$') {
//                    demo1.append(a);
//                    while (test.charAt(i) != ';' || i < test.length()) {
//                        demo1.append(test.charAt(i));
//                        i++;
//                    }
//                    if (demo1.indexOf("$var") >= 0) {
//                        //Todo转换为user名臣
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$s") >= 0) {
//                        // 设定操作对象的shell
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$p") >= 0) {
//                        //单独使用暂停说话，ani中使用暂停
//                        continue;
//                    }
//
//
//
//                    if (demo1.indexOf("$em") >= 0) {
//                        //Todo设定图层，换表情
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$ani") >= 0) {
//                        //Todo标记动画开始，不允许嵌套
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$_ani") >= 0) {
//                        //Todo标记动画结束
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$sp") >= 0) {
//                        //Todo字符上屏速度，不设定则使用默认，仅影响此speak中此标记之后的文字
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$brani") >= 0) {
//                        //Todo打断动画
//                        continue;
//                    }
//                    if (demo1.indexOf("$$") >= 0) {
//                        //Todo解析为一个文本的$
//                        continue;
//                    }
//                    if (demo1.indexOf("$n") >= 0) {
//                        //Todo换行
//                        continue;
//                    }
//
//                } else {
//                    while (test.charAt(i) != '$' || i < test.length()) {
//                        demo2.append(a);
//                        i++;
//                    }
//
//                    area.setText(demo2.toString());//在气球上输出内容
//                    new Thread(new Runnable() {
//                        public void run() {                                                          
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(JUkaGhost.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            }
//                        
//                    }).start();
//                }
//                i++;
//                demo1 = null;
//                demo2 = null;
//
//            }
//
//            return 0;
//        }
//    }

    /**
     * 
     */
    public static void main(String[] args) {
        //以下测试
         CyauShell.onLoad();
        CyauShell tmpshell = CyauShell.createShell();
        
        UkagakaWin tmpukawin = tmpshell.getUkagaka();
        //BalloonWin testballoon = tmpshell.createBalloon();

        tmpukawin.setVisible(true);
        tmpukawin.repaint();
        tmpukawin.clip();
        return;
    }
}
