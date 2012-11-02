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
 * �����Ϊ����speak��ĵ�һ�棬�޸��Ե�һ��CyauGhost,�������м����˺���͹���
 * ����֮����ʺϼ�����GhostOperatingSystem�࣬��Ȼ����Ghost��Ҳ�����У�����ֻ��
 * ��Ϊ���Թ��ܹ������²۵����á�
*/
public class Speak extends JPanel {

    private JTextArea area = new JTextArea();
    private String[] tucaoText = GetTucaoText();
    private int time;
    private int lth;
    int delay = 1500; //milliseconds
    final int seconddelay = 4000;//�ڶ���ʱ��
    Timer timer;

    public Speak() {
    }

    protected void initalizeSpeak() {
        try {
            lth = GetTucaoLength();//Tucao.txt������
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Speak.class.getName()).log(Level.SEVERE, null, ex);
        }
        int delay = 6000; //milliseconds
        final int seconddelay = 4000;//�ڶ���ʱ��

        timer = new Timer(delay, new timelistener());


    }

    private static void doinitalizeSpeak() {
    }

    class timelistener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
           
            time = (int) (Math.random() * lth);            //�����������
            SpeakCtrl();
            area.setLineWrap(true);
            area.setEditable(false);
            area.setWrapStyleWord(true);
            add(area, BorderLayout.CENTER);
          
            
            ActionListener taskPerformer1 = new ActionListener() {

                @Override////�ڶ���ʱ��
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    //   shell.destroyBalloon(newballoon);
                }
            };

            new Timer(seconddelay, taskPerformer1).start();
        }
    }

    //
    public void CheckTucaoFile(File file) {//���Tucao.txt�Ƿ����

        if (!file.exists()) {//���Tucao.txt�Ƿ����
            System.out.println("��ȡ�²��ļ�ʧ��");
            System.exit(0);
        }
    }

    public int GetTucaoLength() throws FileNotFoundException {//��ȡTucao.txt������

        int length = 0;
        File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");
        CheckTucaoFile(speakfile);
        try (Scanner input = new Scanner(speakfile)) {
            while (input.hasNextLine()) {
                length++;
                input.nextLine();//��inputɨ�����Ƶ���һ��
            }
        }


        return length;
    }

    public String[] GetTucaoText() {//��Tucao.txt�е��ı���ȡ���浽һ���ַ���������


        String username = null;//�û���

        //username = log.getUsername();//��ȡ�û���

        File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");//��ȡ�ı�
        CheckTucaoFile(speakfile);//����ı��Ƿ����
        String[] tucaotext = new String[lth];//�����ı��������ȵ��ַ�������
        Scanner input = null;//��ʼ��ɨ����
        try {
            input = new Scanner(speakfile);
        } catch (FileNotFoundException ex) {
        }
        int i = 0;//����tucaotext�ַ��������±�
        while (input.hasNextLine()) {//�Ƿ������һ��
            String tempstring = input.nextLine();//��ȡTucao.txt�е�һ���ַ���
            tucaotext[i] = tempstring;//���������ַ����浽tucaotext�ַ���������
            i++;
        }
        tucaoText = tucaotext;
        return tucaotext;
    }
//�²���忪��

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
                    //Todoת��Ϊuser����
                }


                if (demo1.indexOf("$s") >= 0) {
                   // �趨���������shell
                }


                if (demo1.indexOf("$p") >= 0) {
                    //����ʹ����ͣ˵����ani��ʹ����ͣ
                }



                if (demo1.indexOf("$em") >= 0) {
                    //Todo�趨ͼ�㣬������
                }


                if (demo1.indexOf("$ani") >= 0) {
                    //Todo��Ƕ�����ʼ��������Ƕ��
                }


                if (demo1.indexOf("$_ani") >= 0) {
                    //Todo��Ƕ�������
                }


                if (demo1.indexOf("$sp") >= 0) {
                    //Todo�ַ������ٶȣ����趨��ʹ��Ĭ�ϣ���Ӱ���speak�д˱��֮�������
                }


                if (demo1.indexOf("$brani") >= 0) {
                    //Todo��϶���
                }
                if (demo1.indexOf("$$") >= 0) {
                    //Todo����Ϊһ���ı���$
                }
                if (demo1.indexOf("$n") >= 0) {
                    //Todo����
                }

            } else {
                while(test.charAt(i)!='$'||i<test.length()){
                demo2.append(a);
                i++;
                }
                area.setText(demo2.toString());//���������������
                
            }
            i++;
            demo1=null;
            demo2=null;

        }
               
        return 0;
    }
    //////���²���

    public static void main(String[] args) throws FileNotFoundException {
        //���²���
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