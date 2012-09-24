/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagaka.shell;

import jukagaka.*;

import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.geom.Area;
import javax.swing.JDialog;

public class JUkaShell extends JUkaComponent implements Serializable
{
  // Static Data (NEED REWRITE) | 待重写静态数据域
    // 请将这些数据域复制到你重写的子类中, 并按需要修改其中的字符串内容.
    // 非字符串值及数据域签名请勿改动.

    /**
     * <p>此数据域记录 Shell 的静态参数存储, 也就是 ini 文件的位置.</p>
     * <p>Shell 的大部分静态参数(例如春菜的名字, 图像资源列表等)均存储在此 ini
     * 文件中, 通过这种方式存储是为了在便利性和灵活性上取得均衡, 某些 API 会要
     * 求以整个 Shell 类来作为传入参数, 一般它们将通过此域来自行获取参数, 以
     * 简化参数列, 并降低调用者的编程负担.<br>
     * (*) 此类的扩展者应重写这个域</p>
     */
    public static final String DEFAULT_INI = JUkaUtility.getProgramPath() + "defaultShell.ini";

    /**
     * <p>hashImages 和 hashShape 数据域用于挂载缓冲的图像资源</p>
     * <p>此二数据域由 JUkaShellCtrl.prefetchImageResource() 负责写入</p>
     */
    public static Hashtable<String, Image> hashImages = null;
    public static Hashtable<String, Area> hashMasks = null;

  // Instance Data | 实例数据域
    /**
     * 此数据域记录当前可用的气球队列
     */
    private ArrayList<JDialog> winList = null;
    /**
     * 此数据域记录承载春菜的 Frame
     */
    private UkagakaWin ukagakaWin = null;
    /**
     * 此数据域记录主菜单的 Frame
     */
    private BalloonWin mainBalloon = null;
    /**
     * 此数据域标记 Shell 是否已经废弃
     */
    private boolean discarded = false;

  // Shell Instance | Shell 实例管理
    // 请按照要求重写这些方法.
    /**
     * <p>此方法用于产生一个新的 Shell</p>
     * <p>传入的参数将以引用(而非克隆)被存储于生成的 Wins 中, 也就是说对图像库
     * 所作的修改会自动反映到窗体中.<br>
     * (*) 此类的扩展者应以 public 重写方法, 并在自撰的代码<b>前</b>调用父类的方法</p>
     */
    protected static JUkaShell createShell(String argIniFile,Hashtable<String, Image> argHtImages, Hashtable<String, Area> argHtMasks)
    {
        JUkaShell newShell = new JUkaShell();

        newShell.winList = new ArrayList<JWindow>(16);
        newShell.ukagakaWin = UkagakaWin.createUkagaka(argIniFile, argHtImages, argHtMasks);
        newShell.winList.add(newShell.ukagakaWin);
        newShell.mainBalloon = BalloonWin.createBalloon(argIniFile, argHtImages, argHtMasks);
        newShell.winList.add(newShell.mainBalloon);

        return(newShell);
    }

    /**
     * <p>此方法用于弃置一个 Shell 使之不再有效</p>
     * <p>弃置 Shell 是指将其从当前运行环境中销毁, <span color="red">而不是指
     * 将春菜从记录中不可逆地消除.</span><br>
     * 此操作将销毁 Shell 占用的显示资源, 并将其标示为不可用(isDiscarded() 方法
     * 返回 false)<br>
     * 任何对已弃置 Shell 的操作请求都将被驳回或无效.<br>
     * (*) 此类的扩展者应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法</p>
     */
    protected static void destroyShell(JUkaShell argShell)
    {

        argShell.discarded = true;
        return;
    }

    /**
     * <p>此方法用于回收 Shell 使之不再有效</p>
     * <p>此方法只是单纯地调用 JUkaShell.destroyShell(this)<br>
     * (*) 建议此类的扩展者应以 public 重写方法, 并在自撰的代码<b>后</b>调用 super.discard()</p>
     */
    protected void discard()
    {
        JUkaShell.destroyShell(this);
        return;
    }

    /**
     * <p>此方法返回 Shell 的 discarded 域, 以指示该 Shell 是否已经弃置</p>
     * <p>弃置的 shell 是指已经通过 JUkaShell.destroyShell() 处理的 Shell.</p>
     * @return 如果 Shell 已弃置, 则返回 true.
     */
    public boolean isDiscarded()
    {
        return(this.discarded);
    }

  // Ukagaka Control | 春菜操纵命令
    /* 基于 JLS 规范以及锁定机能的需要对 ukagakaWin 进行屏蔽,
     * 除了第一个命令外, 其余命令将被转发到 JUkaShell.ukagakaWin 的同名方法上执行.
     * 此类的扩展者可以重写并以 super.methodName() 的方式调用以增加自己设定的限制.
     * 当然也可以放着不重写, 根据 JLS 的动态绑定设定, 这些方法依旧会被执行.
     */
    /**
     * <p>获取 UkagakaWin 的引用</p>
     * <p>(!) 注意通过此方法可以绕过封装直接操控春菜, 这可能做成破坏性后果,
     * 因此请不要处于调试以外的目的使用此方法.</p>
     */
    public UkagakaWin getUkagaka()
    {
        return(this.ukagakaWin);
    }

    public boolean setImageLayer(String argHashKey, int argLayer, int x, int y, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageLayer(argHashKey, argLayer, x, y));
    }

    public boolean setImageLayer(String argHashKey, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageLayer(argHashKey));
    }

    public boolean setImageBatch(String[] arrHashKeys, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageBatch(arrHashKeys));
    }

    public boolean setImageBatch(String arrHashKeys, int accessKey)
    {
        if (!this.checkAuthority(accessKey))
            return(false);

        return(this.ukagakaWin.setImageBatch(arrHashKeys));
    }

    public int getBufferLayer()
    {
        return(this.ukagakaWin.getBufferLayer());
    }

    public int setBufferLayer(int newBufferLayer)
    {
        return(this.ukagakaWin.setBufferLayer(newBufferLayer));
    }

    public void buildBackBuffer()
    {
        this.ukagakaWin.buildBackBuffer();
        return;
    }

  // Balloon Control | 气球操纵指令
    // STILL IN PROCESS, WILL COME OUT SOON

  // General for Ukagaka & Balloon | Win 通用操纵命令
    // STILL IN PROCESS, WILL COME OUT SOON

    //public void clip(JWindow argTargetWin)
    //public void repaint(JWindow argTargetWin)

  // Monopoly | 独占使用(吐槽坑爹的爱词霸)
    /**
     * 此数据域记录目前使用中的 Key
     */
    private int preservedKey = 0;

    /**
     * <p>锁定春菜以获得其专用权</p>
     * <p>此类的扩展者应以 public 覆盖此函数, 并在自撰的代码<strong>后</strong>
     * 调用此方法.</p>
     */
    protected int lockUkagaka()
    {
        // 已锁定时报错
        if (this.preservedKey != 0)
            return(0xFFFFFFFF);

        // 生成密钥
        do
            this.preservedKey = new Double(Math.random() * 0x7FFFFFFF).intValue();
        while (this.preservedKey <= 0);

        return(this.preservedKey);
    }

    /**
     * <p>解锁伪春菜以去除其专用保护</p>
     * <p>由于不依赖密钥解锁, 所以这个方法总是成功并且返回 true 的, 这意味着
     * 可以抢占 Shell, 而且不会有栈信息以还原抢占...<br>
     * 此类的扩展者应该以 public 覆盖此方法, 并在自撰的代码<strong>后</strong>
     * 调用此方法</p>
     */
    protected boolean unlockUkagaka()
    {
        this.preservedKey = 0;

        return(true);
    }

    /**
     * <p>此方法判定给定的密钥能否获得春菜的占用权</p>
     */
    public boolean checkAuthority(int key)
    {
        if (this.preservedKey == 0)
            return(true);

        return((key ^ this.preservedKey) == 0);
    }

  // Start-up | 启动器
    /**
     * <p>此方法目前不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>子类的该方法会在 JUkagaka 启动时被调用, 组件可以趁此完成初始化工作<br>
     * 如果子类要表示不能正常加载, 抛出任意 Exception 即可.<br>
     * (*) 此类的扩展这应以 public 重写方法, 并在自撰的代码<strong>前</strong>调用此方法<br></p>
     */
    protected static void onLoad()
    {

        return;
    }

    /**
     * <p>此方法用于退出时调用</p>
     * <p>子类的该方法会在 JUkagaka 退出前被调用, 组件可以趁此完成退出前工作<br>
     * 如果子类要表示不能正常加载, 抛出任意 Exception 即可.<br>
     * (*) 此类的扩展这应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法<br></p>
     */
    protected static void onExit()
    {
        return;
    }

  // Other | 杂项
    /**
     * 构造函数...不应该被显式调用
     */
    protected JUkaShell()
    {
    }
    /**
     * @deprecated 此方法目前仅用于调试
     */
    public static void main(String[] args)
    {
        return;
    }
}

