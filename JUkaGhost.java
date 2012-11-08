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
  // Shell Manipulate | Shell �ٿ�
    /**
     * <p>��Ҫ Shell</p>
     * <p>��Ҫ Shell ��ָ������ģ��δָ������ Shell ����¹�����ȱʡ Shell,
     * ��ͨ��ָ�� assignedShell[0].<br>
     * ��Ȼ�ڴ�ƽ̨��û�ж���, �� masterShell һ���Ӧ�ڴ�ͳ��"���˸�"</p>
     */
    protected JUkaShell masterShell;

    /**
     * <p>���� Shell</p>
     * <p>��ͨ��ָ�� assignedShell[1].<br>
     * ��Ȼ�ڴ�ƽ̨��û�ж���, �� slaveShell һ���Ӧ�ڴ�ͳ��"ʹħ"</p>
     */
    protected JUkaShell slaveShell;


	 transient private static Object argghost;


    protected GhostOperatingSystem mainGhost;
    /**
     * <p>���ӵ� Shell �ļ�¼��</p>
     * <p>ע�Ⲣ�Ǳ���������������(����ֻ��һ�� Shell ʱ), ���������Ӧ����д
     * getAssignedShell() ����ȷ�ط�������� Shell ����.</p>
     */
    protected ArrayList<JUkaShell> assignedShell = null;

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
     * <p>�˷����������κδ���, ������Ϊ token ����.</p>
     * <p>(*) �κ���չ��������඼�����Դ˷���ǩ����д�÷���<br>
     * ����ĸ÷����������� JUkaComponentCtrl ��ʼ����ɺ󱻵���, ������ Ghost ��ʼ����</p>
     */
    protected static void onStart()
    {
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

  // Other | ����
    

	//�洢Ghost�˸��Լ�����Shell,������


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
 * �����Ϊ����speak��ĵ�һ�棬�޸��Ե�һ��CyauGhost,�������м����˺���͹���
 * ����֮����ʺϼ�����GhostOperatingSystem�࣬��Ȼ����Ghost��Ҳ�����У�����ֻ��
 * ��Ϊ���Թ��ܹ������²۵����á�
*/
//public class Speak extends JPanel {
//    private int lth = GetTucaoLength();
//    private JTextArea area = new JTextArea();
//    private String[] tucaoText = GetTucaoText();
//    private int time;
//    
//    int delay = 1500; //milliseconds
//    final int seconddelay = 4000;//�ڶ���ʱ��
//    Timer timer;
//
//    public Speak() throws FileNotFoundException{
//        initalizeSpeak();
//    }
//
//    protected void initalizeSpeak() {
//
//        int delay = 6000; //milliseconds
//        final int seconddelay = 4000;//�ڶ���ʱ��
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
//                time = (int) (Math.random() * lth);            //�����������
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
//                    @Override////�ڶ���ʱ��
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
//        public void CheckTucaoFile(File file) {//���Tucao.txt�Ƿ����
//
//            if (!file.exists()) {//���Tucao.txt�Ƿ����
//                System.out.println("��ȡ�²��ļ�ʧ��");
//                System.exit(0);
//            }
//        }
//
//        public int GetTucaoLength() throws FileNotFoundException {//��ȡTucao.txt������
//
//            int length = 0;
//            File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");
//            CheckTucaoFile(speakfile);
//            try (Scanner input = new Scanner(speakfile)) {
//                while (input.hasNextLine()) {
//                    length++;
//                    input.nextLine();//��inputɨ�����Ƶ���һ��
//                }
//            }
//
//
//            return length;
//        }
//
//        public String[] GetTucaoText() {//��Tucao.txt�е��ı���ȡ���浽һ���ַ���������
//
//
//            String username = null;//�û���
//
//            //username = log.getUsername();//��ȡ�û���
//
//            File speakfile = new File(JUkaUtility.getProgramDir() + "Tucao.txt");//��ȡ�ı�
//            CheckTucaoFile(speakfile);//����ı��Ƿ����
//            String[] tucaotext = new String[lth];//�����ı��������ȵ��ַ�������
//            Scanner input = null;//��ʼ��ɨ����
//            try {
//                input = new Scanner(speakfile);
//            } catch (FileNotFoundException ex) {
//            }
//            int i = 0;//����tucaotext�ַ��������±�
//            while (input.hasNextLine()) {//�Ƿ������һ��
//                String tempstring = input.nextLine();//��ȡTucao.txt�е�һ���ַ���
//                tucaotext[i] = tempstring;//���������ַ����浽tucaotext�ַ���������
//                i++;
//            }
//            tucaoText = tucaotext;
//            return tucaotext;
//        }
////�²���忪��
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
//                        //Todoת��Ϊuser����
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$s") >= 0) {
//                        // �趨���������shell
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$p") >= 0) {
//                        //����ʹ����ͣ˵����ani��ʹ����ͣ
//                        continue;
//                    }
//
//
//
//                    if (demo1.indexOf("$em") >= 0) {
//                        //Todo�趨ͼ�㣬������
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$ani") >= 0) {
//                        //Todo��Ƕ�����ʼ��������Ƕ��
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$_ani") >= 0) {
//                        //Todo��Ƕ�������
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$sp") >= 0) {
//                        //Todo�ַ������ٶȣ����趨��ʹ��Ĭ�ϣ���Ӱ���speak�д˱��֮�������
//                        continue;
//                    }
//
//
//                    if (demo1.indexOf("$brani") >= 0) {
//                        //Todo��϶���
//                        continue;
//                    }
//                    if (demo1.indexOf("$$") >= 0) {
//                        //Todo����Ϊһ���ı���$
//                        continue;
//                    }
//                    if (demo1.indexOf("$n") >= 0) {
//                        //Todo����
//                        continue;
//                    }
//
//                } else {
//                    while (test.charAt(i) != '$' || i < test.length()) {
//                        demo2.append(a);
//                        i++;
//                    }
//
//                    area.setText(demo2.toString());//���������������
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
        //���²���
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
