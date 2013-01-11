// This file is created by Editplus

package jukagaka.ghost;

import java.io.Serializable;

abstract class LimitedMeter implements Serializable
{
  // Master | ��Ҫ
    private String name;
    public int value;
    private int minLimit;
    private int maxLimit;

    /**
     * <p>�������ķ�ʽ�޸ļ�����, ����ָ������, ���ռ���������Ч��Լ��</p>
     */
    public int modify(int increment)
    {
        this.value += increment;
        this.bind();

        return(this.value);
    }

    /**
     * <p>ִ����Ч��Լ��, ������Ч���value�ᱻ�ضϵ�������Сֵ</p>
     * <p>�κ�ͨ��������value���޸Ķ��Զ������˴˺���, ��˲����ֶ�������</p>
     */
    public void bind()
    {
        if (this.value < this.minLimit)
            this.value = this.minLimit;
        if (this.value > this.maxLimit)
            this.value = this.maxLimit;

        return;
    }

  // Get/Set | Getter��Setter
    /**
     * <p>���ؼ���������</p>
     */
    public String getString()
    {
        return(this.name);
    }
    /**
     * <p>���ص�ǰ�Ĵ洢��value</p>
     */
    public int getValue()
    {
        return(this.value);
    }

    /**
     * <p>ʹ���趨ֱֵ������ִ� value, ����Ч��Լ��.</p>
     */
    public int setValue(int argValue)
    {
        this.value = argValue;
        this.bind();

        return(this.value);
    }

  // Construct & Destruct | ���켰����
    /**
     * <p>����һ����������ļ�����</p>
     * <p>�޲ι��췽��, �������л�ʹ��</p>
     */
    public LimitedMeter()
    {
    }

    public LimitedMeter(String argName)
    {
        this(argName, 0, 0, 255);
    }

    /**
     * <p>����һ����������ļ�����</p>
     * <p>��Ч��LimitedMeter(argName, initValue, 0, 225)</p>
     * @param initValue�Ǹ����˼������ĳ�ֵ
     */
    public LimitedMeter(String argName, int initValue)
    {
        this(argName, initValue, 0, 255);
    }

    /**
     * <p>����һ����������ļ�����, ����Ч��Լ��</p>
     * @param initValue�Ǹ����˼������ĳ�ֵ
     * @param argMinLimitָ����Ч���½�
     * @param argMaxLimitָ����Ч���Ͻ�
     */
    public LimitedMeter(String argName, int initValue, int argMinLimit, int argMaxLimit)
    {
        this.name = argName;
        this.value = initValue;
        this.minLimit = argMinLimit;
        this.maxLimit = argMaxLimit;

        this.bind();
    }

  // Others | ����
    /**
     * @deprecated �˷���Ŀǰ�����ڵ���
     */
    //public static void main(String[] args)
    //{
        //return;
    //}
}
