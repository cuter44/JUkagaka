// This file is created by Editplus

package jukagaka.ghost;

import java.io.Serializable;

abstract class LimitedMeter implements Serializable
{
  // Master | 主要
    private String name;
    public int value;
    private int minLimit;
    private int maxLimit;

    /**
     * <p>以增量的方式修改计数器, 可以指定负数, 最终计算结果受有效域约束</p>
     */
    public int modify(int increment)
    {
        this.value += increment;
        this.bind();

        return(this.value);
    }

    /**
     * <p>执行有效域约束, 超过有效域的value会被截断到最大或最小值</p>
     * <p>任何通过函数对value的修改都自动调用了此函数, 因此不用手动调用它</p>
     */
    public void bind()
    {
        if (this.value < this.minLimit)
            this.value = this.minLimit;
        if (this.value > this.maxLimit)
            this.value = this.maxLimit;

        return;
    }

  // Get/Set | Getter和Setter
    /**
     * <p>返回计数器名字</p>
     */
    public String getString()
    {
        return(this.name);
    }
    /**
     * <p>返回当前的存储的value</p>
     */
    public int getValue()
    {
        return(this.value);
    }

    /**
     * <p>使用设定值直接替代现存 value, 受有效域约束.</p>
     */
    public int setValue(int argValue)
    {
        this.value = argValue;
        this.bind();

        return(this.value);
    }

  // Construct & Destruct | 构造及析构
    /**
     * <p>构建一个有限制域的计数器</p>
     * <p>无参构造方法, 留给序列化使用</p>
     */
    public LimitedMeter()
    {
    }

    public LimitedMeter(String argName)
    {
        this(argName, 0, 0, 255);
    }

    /**
     * <p>构建一个有限制域的计数器</p>
     * <p>等效于LimitedMeter(argName, initValue, 0, 225)</p>
     * @param initValue是赋给此计数器的初值
     */
    public LimitedMeter(String argName, int initValue)
    {
        this(argName, initValue, 0, 255);
    }

    /**
     * <p>构建一个有限制域的计数器, 受有效域约束</p>
     * @param initValue是赋给此计数器的初值
     * @param argMinLimit指定有效域下界
     * @param argMaxLimit指定有效域上界
     */
    public LimitedMeter(String argName, int initValue, int argMinLimit, int argMaxLimit)
    {
        this.name = argName;
        this.value = initValue;
        this.minLimit = argMinLimit;
        this.maxLimit = argMaxLimit;

        this.bind();
    }

  // Others | 杂项
    /**
     * @deprecated 此方法目前仅用于调试
     */
    //public static void main(String[] args)
    //{
        //return;
    //}
}
