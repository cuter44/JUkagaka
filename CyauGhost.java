class  CyauGhost extends JUkaGhost
{
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
    public static void onLoad()
    {
        return;
    }

    /**
     * <p>此方法不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>(*) 任何扩展此类的子类都必须以此方法签名重写该方法<br>
     * 子类的该方法会在所有 JUkaComponentCtrl 初始化完成后被调用, 以引导 Ghost 开始工作</p>
     */
    public static void onStart()
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

  // Other | 杂项
    

	//存储Ghost人格以及部分Shell

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
