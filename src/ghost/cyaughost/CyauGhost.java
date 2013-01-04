package jukagaka.ghost.cyaughost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jukagaka.Ghost.*;

import jukagaka.shell.JUkaShell;
import jukagaka.shell.UkagakaWin;
import jukagaka.shell.cyaushell.CyauShell;

class  CyauGhost extends JUkaGhost
{
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
	String ghostargFile = JUkaUtility.getProgramDir()+"ghost/JUkaGhost1.txt";

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
		JUkaGhost.onload();
		
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
		JUkaGhost.onstart();
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
           cyauGhost.initalizeGhost(ghostargFile);
        } catch (IOException ex) {
            Logger.getLogger(CyauGhost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CyauGhost.class.getName()).log(Level.SEVERE, null, ex);
        }
        //���nullֵҲΪ�ļ��������������ڴ���ʱ���������ļ���
		//�÷�����Ϊ����ʱ������ʼGhostʹ��
		cyauGhost.createGhost(cyauGhost,ghostargFile);

		return cyauGhost;
     }
  // Other | ����

	//�洢Ghost�˸��Լ�����Shell



	public static void main(String[] args) 
	{
		
	}
}
