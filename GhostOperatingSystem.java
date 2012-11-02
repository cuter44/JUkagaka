/*版本编号——V2 代号——维罗妮卡
 * V1版本：新增：级数、心情、亲密度、保存、 读取
 * V2版本：新增：礼物界面
 * 		   删除：通常界面
 */
package jukagaka.ghost;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Date;
import javax.swing.*;


/*版本编号——V2 代号——维罗妮卡
 * V1版本：新增：级数、心情、亲密度、保存、 读取
 * V2版本：新增：礼物界面
 * 		   删除：通常界面
 */
public class GhostOperatingSystem implements Serializable {
	//private static int closeMAX = 100;										   //亲密度最大值
	//private static int expMAX = Integer.MAX_VALUE;							   //经验最大值
	//private static int timeTrigger = 100;									   //触发时间
	
	//private int age;														   //年龄（级数）
	//private int exp;														   //成长（经验）
	//private int close;														   //亲密度
	//private long leave;														   //记录下线时间
	//private String mood;													   //心情
	
	//private Timer timer = new Timer(timeTrigger, new TimerListener());	
	 

    public GhostOperatingSystem(){
	}

	public void initalizeOS(){
		//int dayLeft = (int)((getTime() - getLeaveTime())) / 24;
		//if(dayLeft >= 1)
			//closeDown(dayLeft * 10);
		//close=0;
		//timer.start();
		//this.add(new barChart());
      }



	
	//public void onExit() {
		//leave = getTime();
		//age = getAge();
		
		////保存文件文件
	//}
	
	///*增加亲密度 
	 //*
	 //* 一般情况下，亲密度只能增加到（closeMAX-10），只有使用 道具 才能增加到closeMAX
	 //* 一般情况加亲密时 i == 0
	 //* 使用道具 加亲密时 i == 1
	 //*/
	//public void closeUp(int n, int i) {
		//if(i == 0) {
			//if(close + n <= closeMAX - 10)
				//close += n;
			//else
				//close = closeMAX - 10;
		//}
		//else if(i == 1) {
			//if(close + n <= closeMAX)
				//close += n;
			//else
				//close = closeMAX;
		//}
			
	//}
	
	////减少亲密度
	//public void closeDown(int n) {
		//if(close - n >= 0)
			//close -= n;
		//else
			//close = 0;
	//}
	
	///*增加经验值
	 //*
	 //* 每在线一小时 +1
	 //*/
	//public void expUp(int n) {
		//if(exp + n < expMAX)
			//exp += n;
		//else
			//exp = expMAX;
	//}
	
	///*计算级数
	 //* 公式: level = ln(exp)/ln(2)
	 //*/
	//public int getAge() {
		//return (int)(Math.log(exp) /  Math.log(2));
	//}
	
	////级数当前级数的最大值 exp					界面时使用
	//public int getLevelExp() {
		//return (int)(Math.pow(2, getAge() + 1));
	//}
	
	////获取当前亲密度
	//public int getClose() {
		//return close;
	//}
	
	////获取时间
	//public long getTime() {
		//Date date = new Date();
		//return date.getTime() / 1000 / 60 / 60;								   //以小时作单位
	//}
	
	////读取上一次离开时间
	//public long getLeaveTime() {
		//return leave;
	//}
	
	///*获取心情
	 //* 心情分11个层次
	 //*/
	//public int getMood() {
		//int m = close / 10;
		//switch(m) {
			//case 0:  mood = "寻死"; break;
			//case 1:  mood = "抑郁"; break;
			//case 2:  mood = "失落"; break;
			//case 3:  mood = "伤心"; break;
			//case 4:  mood = "较差"; break;
			//case 5:  mood = "平静"; break;
			//case 6:  mood = "较好"; break;
			//case 7:  mood = "愉快"; break;
			//case 8:  mood = "开心"; break;
			//case 9:  mood = "高兴"; break;
			//case 10: mood = "大好"; break;
		//}
		//return m;
	//}
	
	////时间监听器
	//private class TimerListener implements ActionListener {
		//public void actionPerformed(ActionEvent e) {
			//expUp(1);
			//repaint();
		//}
	//}
	
	////柱形图										界面用
	//class barChart extends JPanel {
		
		//public barChart() {
			//exp = 1;
			//close = 59;
		//}
		
	    //protected void paintComponent(Graphics g) {
	        //super.paintComponent(g);
	        
	        //int x = getWidth() / 15;
	        //int y = getHeight() / 13;
	        //int l = (getWidth() - 50) / getLevelExp() * exp;
	        //int h = getHeight() / 13 * 2;
	        
	        //g.setColor(Color.WHITE);
	        //g.fillRect(x, y * 3, getWidth() - 50, h);
	        //g.setColor(new Color(255,255,150));
	        //g.fillRect(x, y * 3, l, h);
	        //g.setColor(Color.WHITE);
	        //g.fillRect(x, y * 6, getWidth() - 50, h);
	        //g.setColor(Color.BLUE);
	        //g.fillRect(x, y * 6, ((getWidth() - 50) / closeMAX * close), h);
	        //g.setColor(Color.BLACK);
	        //g.drawString(exp + "/" + getLevelExp() + " " , getWidth() / 15, getHeight() / 13 * 4);
	        //g.drawString("级数：" + getAge(), getWidth() / 15, getHeight() / 13 * 2);
	    //}
	//}
	
	//public static void main(String[] args) {
          //GhostOperatingSystem frame = new GhostOperatingSystem();
           //frame.initalizeOS();

		//frame.setSize(300, 300);
		//frame.setLocationRelativeTo(null);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setVisible(true);
	//}
}