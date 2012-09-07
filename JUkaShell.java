/**
 * @author "Galin"<cuter44@qq.com>
 * @version v120827
 */

package jukagak.shell;

import jukagaka.*;

import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.geom.Area;
import javax.swing.JWindow;

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
    private ArrayList<JWindow> frameList = null;
    /**
     * 此数据域记录承载春菜的 Frame
     */
    private UkagakaFrame ukagakaFrame = null;
    /**
     * 此数据域记录主菜单的 Frame
     */
    private BalloonFrame mainBalloonFrame = null;
    /**
     * 此数据域标记 Shell 是否已经废弃
     */
    private boolean discarded = false;

  // Shell Instance | Shell 实例管理
  // 请按照要求重写这些方法.
    /**
     * <p>此方法用于产生一个新的 Shell</p>
     * <p><br>
     * (*) 此类的扩展者应按照该签名重写方法, 并在自撰的代码<b>前</b>调用父类的方法</p>
     */
    public static JUkaShell createShell()
    {
        JUkaShell newShell = new JUkaShell();
        newShell.ukagakaFrame = UkagakaFrame.createFrame();
        newShell.mainBalloonFrame = balloonFrame.createFrame();
        return(null);
    }

    /**
     * <p>此方法用于回收一个 Shell 使之不再有效</p>
     * <p>此方法将销毁春菜占用的资源<br>
     * (*) 此类的扩展者应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法</p>
     */
    public static void destroyShell()
    {

        this.discarded = true;
        return;
    }

  // Start-up | 启动器
    /**
     * <p>此方法目前不包含任何代码, 仅仅作为 token 存在.</p>
     * <p>子类的该方法会在 JUkagaka 启动时被调用, 组件可以趁此完成初始化工作<br>
     * 如果子类要表示不能正常加载, 抛出任意 Exception 即可.<br>
     * (*) 此类的扩展这应按照该签名重写方法<br></p>
     */
    public static void onLoad()
    {
        return;
    }

    /**
     * <p>此方法用于退出时调用</p>
     * <p>子类的该方法会在 JUkagaka 退出前被调用, 组件可以趁此完成退出前工作<br>
     * (*) 此类的扩展这应按照该签名重写方法, 并在自撰的代码<b>后</b>调用父类的方法<br></p>
     */
    public static void onExit()
    {
        return;
    }

  // Other | 其他
    /**
     * 不允许访问的构造函数
     */
    private JUkaShell()
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

