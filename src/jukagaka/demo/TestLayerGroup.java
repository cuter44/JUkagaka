package jukagaka.demo;

import jukagaka.shell.UkaWindow;

public class TestLayerGroup
{
    public static void main(String[] args)
    {
        UkaWindow window = new UkaWindow();
        UkaWindow.LayerGroup lGroup = window.getLayers();

        return;
    }
}

/*
 * 结论:
 * 可以用这样的语法使用内部类
 */
