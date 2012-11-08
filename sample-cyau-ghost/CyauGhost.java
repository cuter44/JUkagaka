package jukagaka.ghost.cyaughost;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import jukagaka.ghost.*;
import jukagaka.JUkaUtility;
import jukagaka.shell.BalloonWin;
import jukagaka.JUkaGhostCtrl;
import jukagaka.shell.JUkaShell;
import jukagaka.shell.UkagakaWin;
import jukagaka.shell.cyaushell.CyauShell;

public class  CyauGhost extends JUkaGhost
{
    public CyauGhost(){
        try {
            Speak tmpSpeak = new Speak();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CyauGhost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	    /**
     * <p>��Ҫ Shell</p>
     * <p>��Ҫ Shell ��ָ������ģ��δָ������ Shell ����¹�����ȱʡ Shell,
     * ��ͨ��ָ�� assignedShell[0].<br>
     * ��Ȼ�ڴ�ƽ̨��û�ж���, �� masterShell һ���Ӧ�ڴ�ͳ��"���˸�"</p>
     */
    protected CyauShell masterShell;

    /**
     * <p>���� Shell</p>
     * <p>��ͨ��ָ�� assignedShell[1].<br>
     * ��Ȼ�ڴ�ƽ̨��û�ж���, �� slaveShell һ���Ӧ�ڴ�ͳ��"ʹħ"</p>
     */
    //protected JUkaShell slaveShell;

    
    protected GhostOperatingSystem mainGhost;
    /**
     * <p>���ӵ� Shell �ļ�¼��</p>
     * <p>ע�Ⲣ�Ǳ���������������(����ֻ��һ�� Shell ʱ), ���������Ӧ����д
     * getAssignedShell() ����ȷ�ط�������� Shell ����.</p>
     */
    protected ArrayList<JUkaShell> assignedShell = null;

    /**
	 *��Ϊ��¼����Ghost���ļ���
	 *�ļ����ô����ʹ�ָ��Ghost
	 */
	static String ghostargFile = JUkaUtility.getProgramDir()+"/ghost/JUkaGhost1.txt";

    /**
     * <p>����ָ������Ż�ȡ�� Ghost ���Ƶ� Shell </p>
     */
    public JUkaShell getAssignedShell(int argShellID)
    {
        if (argShellID >= this.assignedShell.size())
        {
            System.err.println("Error: JUkaGhost.getAssignedShell(): ���� argShellID ����: " + argShellID);
            return(null);
        }
        return(this.assignedShell.get(argShellID));

        // ��ʹ�� master/slave ģʽ(��ʹ�� assignedShell ����)ʱ, Ӧ�ð��մ������
        // ���� masterShell �� slaveShell, �ο����������:
        //if (argShellID == 0)
            //return(this.masterShell);
        //if (argShellID == 1)
            //return(this.slaveShell);

        // ��ֻ�е��� shell(��ʹ�� assignedShell ����)ʱ, Ӧ���Ƿ��� masterShell
        // �ο���������:
        //return(this.masterShell);
    }

  // Install/Uninstall | ��װ/ж��
    // �밴��Ҫ����д��Щ����.
    /**
     * <p>(ģ��)��װ Ghost ���</p>
     * <p>Ghost �ڿ��Ա�ʹ��֮ǰ, ������ JUkaStage ֪ͨ�����<br>
     * (!) �������չ��Ӧ<b>����</b>����������� TODO �������׫�Ĵ���,
     *     JUkaGhost.java ��ԭ�еĲ��ֲ��ܱ�ɾ��. ������ܵ�������ʧ��.<br>
     * </p>
     * @return <ul>����ֵ, ���ڵ���0����ʾִ�гɹ�, ��֮��ʾʧ��
     *  <li>0=����
     *  <li>1=������Ѱ�װ
     *  <li>-1=(δ�������)
     *  <li>-2=����ջ����
     *  <li>-3=��д����
     * </ul>
     */
    public static int install()
    {
        // TODO �ڴ˴����밲װǰ�Ĵ���
        // ���� �������/��ͻ��ϵ, EULA, ʹ��˵��, ȷ�϶Ի���, �����û����� �ȵ�.
        return(JUkaGhostCtrl.installComponent());
    }

    /**
     * <p>(ģ��)ж�� Ghost ���</p>
     * <p>Ҫ���ټ���ĳ�� Ghost, ������ JUkaStage ֪ͨע��<br>
     * (!) �������չ��Ӧ<b>����</b>����������� TODO �������׫�Ĵ���,
     *     JUkaGhost.java ��ԭ�еĲ��ֲ��ܱ�ɾ��. ������ܵ�������ʧ��.<br>
     * </p>
     * @return <ul>����ֵ, ���ڵ���0����ʾִ�гɹ�, ��֮��ʾʧ��
     *  <li>0=����
     *  <li>1=����������б���
     *  <li>-1=(δ�������)
     *  <li>-2=����ջ����
     *  <li>-3=��д����
     * </ul>
     */
    public static int uninstall()
    {
        // TODO �ڴ˴�����ж��ǰ�Ĵ���
        // ���� ȷ�϶Ի���, �����û�����, ����ж�صȵ�.
        return(JUkaGhostCtrl.uninstallComponent());
    }

  // Trigger | ������
    /**
     * <p>�˷����������κδ���, ������Ϊ token ����.</p>
     * <p>(*) �κ���չ��������඼�����Դ˷���ǩ����д�÷���<br>
     * ����ĸ÷������� JUkagaka ����ʱ������, ���ṩ������ Ghost ��ɳ�ʼ������</p>
     */
    public static void onLoad() 
    {
		CyauShell.onLoad();
		JUkaGhost.onLoad(ghostargFile);
		
        return;
    }

    /**
     * <p>�˷����������κδ���, ������Ϊ token ����.</p>
     * <p>(*) �κ���չ��������඼�����Դ˷���ǩ����д�÷���<br>
     * ����ĸ÷����������� JUkaComponentCtrl ��ʼ����ɺ󱻵���, ������ Ghost ��ʼ����</p>
     *����CyauGhost��onStart�������ڲ��ԣ����ݴ�˵���ַ��ǳ�����
	 */
    public static void onStart()
    {
		
		JUkaGhost.onStart();
		CyauGhost temGhost = CyauGhost.createGhost();
        CyauShell tmpshell = CyauShell.createShell();
		temGhost.masterShell=tmpshell;
		

        UkagakaWin tmpukawin = tmpshell.getUkagaka();
        tmpukawin.setVisible(true);
        tmpukawin.repaint();
        tmpukawin.clip();
        return;
    }

  // Speak & Marco System | �����
    //   ��
    //  /��\  TODO: ������...
    // /_��_\

    ///**
     //* <p>ָʾ Shell ����ͨ��˵������</p>
     //* <p>�ڽ��������.(����)
     //* </p>
     //* @return ���ս�����˵������
     //*/
    //public String speak(String argSpeech, JUkaShell argShell)
//nullֵΪ�ļ�·����
    public static CyauGhost createGhost(){
		CyauGhost cyauGhost=new CyauGhost();
           
        try {
            //initalize����Ϊ��Ghost�����ļ����ں�ʹ�ã�
                //�������Ϊ�ڳ���ʵ������ʱʹ��
           cyauGhost.initalizeGhost();
        } catch (IOException ex) {
            Logger.getLogger(CyauGhost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CyauGhost.class.getName()).log(Level.SEVERE, null, ex);
        }
        //���nullֵҲΪ�ļ��������������ڴ���ʱ���������ļ���
		//�÷�����Ϊ����ʱ������ʼGhostʹ��
		//cyauGhost.createGhost(cyauGhost);

		return cyauGhost;
     }
  // Other | ����

	//�洢Ghost�˸��Լ�����Shell



class Speak extends JPanel {
    private int lth = GetTucaoLength();
    private JTextArea area = new JTextArea();
    private String[] tucaoText = GetTucaoText();
    private int time;
    
    int delay = 1500; //milliseconds
    final int seconddelay = 4000;//�ڶ���ʱ��
    Timer timer;

    public Speak() throws FileNotFoundException{
        initalizeSpeak();
		
    }

    protected void initalizeSpeak() {

        int delay = 6000; //milliseconds
        final int seconddelay = 4000;//�ڶ���ʱ��

        timer = new Timer(delay, new Speak.timelistener());
 
   timer.start();

    }

        class timelistener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                BalloonWin testBall = masterShell.createBalloon();
				
                time = (int) (Math.random() * lth);            //�����������
                SpeakCtrl(testBall);
			
                area.setLineWrap(true);
                area.setEditable(false);
                area.setWrapStyleWord(true);
                testBall.add(area, BorderLayout.CENTER);
                testBall.setVisible(true);
                testBall.setDragable(true);
               area.repaint();
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

        File speakfile = new File(JUkaUtility.getProgramDir() + "/Tucao.txt");

        CheckTucaoFile(speakfile);
		  
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

            File speakfile = new File(JUkaUtility.getProgramDir() + "/Tucao.txt");//��ȡ�ı�
            CheckTucaoFile(speakfile);//����ı��Ƿ����
            System.out.print(lth);
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


        public void setTucao(boolean word) {
            if (word == true) {
                timer.start();
            } else {
                timer.stop();
            }
        }

        public int SpeakCtrl(BalloonWin testBall) {
            String test = tucaoText[time].trim();
            StringBuilder demo1 = new StringBuilder();
            StringBuilder demo2 = new StringBuilder();
			
            int i = 0;
            while (i < test.length()) {
                char a = test.charAt(i);
		

                if (a == '$') {
                    demo1.append(a);
                    while (test.charAt(i) != ';' || i < test.length()) {
                        demo1.append(test.charAt(i));
                        i++;
                    }
                    if (demo1.indexOf("$var") >= 0) {
                        //Todoת��Ϊuser����
                        continue;
                    }


                    if (demo1.indexOf("$s") >= 0) {
                        // �趨���������shell
                        continue;
                    }


                    if (demo1.indexOf("$p") >= 0) {
                        //����ʹ����ͣ˵����ani��ʹ����ͣ
                        continue;
                    }



                    if (demo1.indexOf("$em") >= 0) {
                        //Todo�趨ͼ�㣬������
                        continue;
                    }


                    if (demo1.indexOf("$ani") >= 0) {
                        //Todo��Ƕ�����ʼ��������Ƕ��
                        continue;
                    }


                    if (demo1.indexOf("$_ani") >= 0) {
                        //Todo��Ƕ�������
                        continue;
                    }


                    if (demo1.indexOf("$sp") >= 0) {
                        //Todo�ַ������ٶȣ����趨��ʹ��Ĭ�ϣ���Ӱ���speak�д˱��֮�������
                        continue;
                    }


                    if (demo1.indexOf("$brani") >= 0) {
                        //Todo��϶���
                        continue;
                    }
                    if (demo1.indexOf("$$") >= 0) {
                        //Todo����Ϊһ���ı���$
                        continue;
                    }
                    if (demo1.indexOf("$n") >= 0) {
                        //Todo����
                        continue;
                    }

                } else {
                    while (test.charAt(i) != '$' && i < test.length()-1) {
						
						a=test.charAt(i);
                        demo2.append(a);
                        i++;
                    }

                    area.setText(demo2.toString());//���������������
                    new Thread(new Runnable() {
                        public void run() {                                                          
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(JUkaGhost.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            }
                        
                    }).start();
                }
                i++;
                demo1.delete(0, demo1.length());
                demo2.delete(0, demo2.length());

            }

            return 0;
        }
    }

//       //���²���
    public static void main(String[] args) {
 
         //CyauShell.onLoad();
//        CyauShell tmpshell = CyauShell.createShell();
//        
//        UkagakaWin tmpukawin = tmpshell.getUkagaka();
//        //BalloonWin testballoon = tmpshell.createBalloon();
         CyauGhost.onLoad();
		 CyauGhost.onStart();
       
 
         


        //tmpukawin.setVisible(true);
        //tmpukawin.repaint();
        //tmpukawin.clip();
        return;
    }
}
