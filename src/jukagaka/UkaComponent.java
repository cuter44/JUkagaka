package jukagaka;

/**
 * 实现了该接口的类称为组件, 继而可以被 UkaDaemon 在特定时刻回调.
 * 实现的同时请实现这些静态方法, 否则...似乎也不会引起宕机...
 * 总之, 随便
 */
public interface UkaComponent
{
  // Launch | 启停接口
  // 这部分函数描述了 Ukagaka 组件系统中应遵守的最基本接口
    /**
     * onLoad() 函数在一个组件被决定加载前被回调.<br />
     * <br />
     * 设计这个函数头的目的是为了让组件(以类而非实例作为考量, 下同)
     * 被正式调用前有机会执行初始化工作(例如读取参数等).<br />
     * 组件返回的哈希值表示组件会被作为是否能成功加载的标志. 一般情况下,
     * 若被调函数返回 true, 表示已经满足运行运行条件, 接下来主调函数将尝试
     * 回调其 onStart() 方法; 若被调函数返回 false, 表示其不能运行, 主调函数
     * 需要这个信息作相应处理.<br />
     */
    //public static abstract boolean onLoad();
    // 默认实现
    //{
        //return(true);
    //}

    /**
     * onStart() 函数在 onLoad() 返回 true, 并且在所有待调用组件完成 onLoad()
     * 时被回调<br />
     * <br />
     * 设计这个函数头的目的是为了让组件能在被调用时决定自身能否顺利执行
     * (例如检查依赖关系)然后正式启动(例如生成实例).<br />
     * 若被调函数返回 true, 表示已顺利而稳定地执行, 主调函数转而处理下一个
     * onStart(); 若被调函数返回 false, 表示其因为某些原因不能运作,
     * 主调函数需要这个信息作相应处理.<br />
     * 注意被调函数要在返回 false 前作相应的善后处理(比如释放内存, 停止自身实例).
     */
    //public static abstract boolean onStart();
    // 默认实现
    //{
        //return(true);
    //}

    /**
     * onExit() 函数在组件决定应该被从内存中卸除时调用.<br />
     * <br />
     * 设计这个函数头的目的是为了让组件在被卸除时能完成清理工作<br />
     * 若被调函数返回 true, 表示已顺利卸除; 若被调函数返回 false, 表示其因为
     * 某些原因无法停止, 主调函数需要这个信息以告知用户.<br />
     */
    //public static abstract boolean onExit();
    // 默认实现
    //{
        //return(true);
    //}

    /**
     * onInstall 函数在被加入到引导列表时被回调.<br />
     * <br />
     * 设计这个函数头的目的是为了让组件在被安装时有机会完成某些支撑部署
     * (例如创建私有配置文件)<br />
     * 虽然要求返回一个布尔值但我还不知道应该怎么使用它...暂时先全部返回
     * true 就好了<br />
     */
    //public static abstract boolean onInstall();
    // 默认实现
    //{
        //return(true);
    //}

    /**
     * onInstall 函数在被从引导列表去除时被回调.<br />
     * <br />
     * 设计这个函数头的目的是为了让组件在被卸除时有机会完成善后工作
     * (例如清理磁盘存储)<br />
     * 虽然要求返回一个布尔值但我还不知道应该怎么使用它...暂时先全部返回
     * true 就好了<br />
     */
    //public static abstract boolean onUnistall();
    // 默认实现
    //{
        //return(true);
    //}

    //public static void main(String[] args)
    //{
        //return;
    //}
}
