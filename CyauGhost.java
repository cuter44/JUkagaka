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
        return;
    }

    /**
     * <p>�˷����������κδ���, ������Ϊ token ����.</p>
     * <p>(*) �κ���չ��������඼�����Դ˷���ǩ����д�÷���<br>
     * ����ĸ÷����������� JUkaComponentCtrl ��ʼ����ɺ󱻵���, ������ Ghost ��ʼ����</p>
     */
    public static void onStart()
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

  // Other | ����
    

	//�洢Ghost�˸��Լ�����Shell

	public void OPStorage(GhostOperatingSystem os)
	{


	}

	public static void OSStorage(GhostOperatingSystem os) throws IOException
	{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("GhostOperatingSystemFile.txt"));


	}
        
        
        
    public static GhostOperatingSystem OSReading() throws IOException, ClassNotFoundException{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("GhostOperatingSystemFile.txt"));
       
            return ((GhostOperatingSystem)input.readObject());
 
           
        }

	public static void main(String[] args) 
	{
		
	}
}
