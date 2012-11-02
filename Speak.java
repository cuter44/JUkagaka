/*
 * Edited by sky_weihao
 * version 1000000
 */

package jukagaka.Ghost;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import jukagaka.JUkaUtility;
import jukagaka.shell.BalloonWin;
import jukagaka.shell.JUkaShell;
import jukagaka.shell.UkagakaWin;
import jukagaka.shell.cyaushell.CyauShell;
/*
 * 这个类为茶兔speak类的第一版，修改自第一个CyauGhost,但是其中加入了宏解释功能
 * 此类之后较适合加入于GhostOperatingSystem类，当然加入Ghost中也能运行，此类只能
 * 作为测试功能供你们吐槽的作用。
*/
public class Speak extends JPanel {

    private JTextArea area = new JTextArea();
    private String[] tucaoText = GetTucaoText();
    private int time;
    private int lth;
    int delay = 1500; //milliseconds
    final int seconddelay = 4000;//第二延时器
    Timer timer;

    public Speak() {
    }

    protected void initalizeSpeak() {
        try {
            lth = GetTucaoLength();//Tucao.txt的行数
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Speak.class.getName()).log(Level.SEVERE, null, ex);
        }
        int delay = 6000; //milliseconds
        final int seconddelay = 4000;//第二延时器

        timer = new Timer(delay, new timelistener());


    }

    private static void doinitalizeSpeak() {
    }

    class timelistener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
           
            time = (int) (Math.random() * lth);            //产生随机内容
            SpeakCtrl();
            area.setLineWrap(true);
            area.setEditable(false);
            area.setWrapStyleWord(true);
            add(area, BorderLayout.CENTER);
          
            
            ActionListener taskPerformer1 = new ActionListener() {

                @Override////第二计时器
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    //   shell.destroyBalloon(newballoon);
                }
            };

            new Timer(seconddelay, taskPerformer1).start();
        }
    }

    //
    public void CheckTucaoFile(File file) {//检查Tucao.txt是否存在

        if (!file.exists()) {//检查Tucao.txt是否存在
            System.out.println("读取吐槽文件失败");
            System.exit(0);
        }
    }

    public int GetTucaoLength() throws FileNotFoundException {//获取Tucao.txt的行数

        int length = 0;
        File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");
        CheckTucaoFile(speakfile);
        try (Scanner input = new Scanner(speakfile)) {
            while (input.hasNextLine()) {
                length++;
                input.nextLine();//将input扫描器移到下一行
            }
        }


        return length;
    }

    public String[] GetTucaoText() {//将Tucao.txt中的文本读取并存到一个字符串数组中


        String username = null;//用户名

        //username = log.getUsername();//获取用户名

        File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");//读取文本
        CheckTucaoFile(speakfile);//检查文本是否存在
        String[] tucaotext = new String[lth];//创建文本行数长度的字符串数组
        Scanner input = null;//初始化扫描器
        try {
            input = new Scanner(speakfile);
        } catch (FileNotFoundException ex) {
        }
        int i = 0;//计算tucaotext字符串数组下标
        while (input.hasNextLine()) {//是否存在下一行
            String tempstring = input.nextLine();//获取Tucao.txt中的一行字符串
            tucaotext[i] = tempstring;//将处理后的字符串存到tucaotext字符串数组中
            i++;
        }
        tucaoText = tucaotext;
        return tucaotext;
    }
//吐槽面板开关

    public void setTucao(boolean word) {
        if (word == true) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public int SpeakCtrl() {
        String test = tucaoText[time].trim();
        StringBuilder demo1 = null;
        StringBuilder demo2 = null;
        int i = 0;
        while (i < test.length()) {
            char a = test.charAt(i);

            if (a == '$') {
                demo1.append(a);
                while (test.charAt(i) != ';'||i<test.length()) {
                    demo1.append(test.charAt(i));
                    i++;
                }
                if (demo1.indexOf("$var") >= 0) {
                    //Todo转换为user名臣
                }


                if (demo1.indexOf("$s") >= 0) {
                   // 设定操作对象的shell
                }


                if (demo1.indexOf("$p") >= 0) {
                    //单独使用暂停说话，ani中使用暂停
                }



                if (demo1.indexOf("$em") >= 0) {
                    //Todo设定图层，换表情
                }


                if (demo1.indexOf("$ani") >= 0) {
                    //Todo标记动画开始，不允许嵌套
                }


                if (demo1.indexOf("$_ani") >= 0) {
                    //Todo标记动画结束
                }


                if (demo1.indexOf("$sp") >= 0) {
                    //Todo字符上屏速度，不设定则使用默认，仅影响此speak中此标记之后的文字
                }


                if (demo1.indexOf("$brani") >= 0) {
                    //Todo打断动画
                }
                if (demo1.indexOf("$$") >= 0) {
                    //Todo解析为一个文本的$
                }
                if (demo1.indexOf("$n") >= 0) {
                    //Todo换行
                }

            } else {
                while(test.charAt(i)!='$'||i<test.length()){
                demo2.append(a);
                i++;
                }
                area.setText(demo2.toString());//在气球上输出内容
                
            }
            i++;
            demo1=null;
            demo2=null;

        }
               
        return 0;
    }
    //////以下测试

    public static void main(String[] args) throws FileNotFoundException {
        //以下测试
         CyauShell.onLoad();
        CyauShell tmpshell = CyauShell.createShell();
        
        UkagakaWin tmpukawin = tmpshell.getUkagaka();
        BalloonWin testballoon = tmpshell.createBalloon(TOOL_TIP_TEXT_KEY, null, null);
        Speak testspeak = new Speak();
       testballoon.add(testspeak);
        tmpukawin.setVisible(true);
        tmpukawin.repaint();
        tmpukawin.clip();
        return;
    }
}