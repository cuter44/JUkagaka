/*�汾��š���V2 ���š���ά���ݿ�
 * V1�汾�����������������顢���ܶȡ����桢 ��ȡ
 * V2�汾���������������
 * 		   ɾ����ͨ������
 */
package jukagaka.ghost;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Date;
import javax.swing.*;


/*�汾��š���V2 ���š���ά���ݿ�
 * V1�汾�����������������顢���ܶȡ����桢 ��ȡ
 * V2�汾���������������
 * 		   ɾ����ͨ������
 */
public class GhostOperatingSystem implements Serializable {
	//private static int closeMAX = 100;										   //���ܶ����ֵ
	//private static int expMAX = Integer.MAX_VALUE;							   //�������ֵ
	//private static int timeTrigger = 100;									   //����ʱ��
	
	//private int age;														   //���䣨������
	//private int exp;														   //�ɳ������飩
	//private int close;														   //���ܶ�
	//private long leave;														   //��¼����ʱ��
	//private String mood;													   //����
	
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
		
		////�����ļ��ļ�
	//}
	
	///*�������ܶ� 
	 //*
	 //* һ������£����ܶ�ֻ�����ӵ���closeMAX-10����ֻ��ʹ�� ���� �������ӵ�closeMAX
	 //* һ�����������ʱ i == 0
	 //* ʹ�õ��� ������ʱ i == 1
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
	
	////�������ܶ�
	//public void closeDown(int n) {
		//if(close - n >= 0)
			//close -= n;
		//else
			//close = 0;
	//}
	
	///*���Ӿ���ֵ
	 //*
	 //* ÿ����һСʱ +1
	 //*/
	//public void expUp(int n) {
		//if(exp + n < expMAX)
			//exp += n;
		//else
			//exp = expMAX;
	//}
	
	///*���㼶��
	 //* ��ʽ: level = ln(exp)/ln(2)
	 //*/
	//public int getAge() {
		//return (int)(Math.log(exp) /  Math.log(2));
	//}
	
	////������ǰ���������ֵ exp					����ʱʹ��
	//public int getLevelExp() {
		//return (int)(Math.pow(2, getAge() + 1));
	//}
	
	////��ȡ��ǰ���ܶ�
	//public int getClose() {
		//return close;
	//}
	
	////��ȡʱ��
	//public long getTime() {
		//Date date = new Date();
		//return date.getTime() / 1000 / 60 / 60;								   //��Сʱ����λ
	//}
	
	////��ȡ��һ���뿪ʱ��
	//public long getLeaveTime() {
		//return leave;
	//}
	
	///*��ȡ����
	 //* �����11�����
	 //*/
	//public int getMood() {
		//int m = close / 10;
		//switch(m) {
			//case 0:  mood = "Ѱ��"; break;
			//case 1:  mood = "����"; break;
			//case 2:  mood = "ʧ��"; break;
			//case 3:  mood = "����"; break;
			//case 4:  mood = "�ϲ�"; break;
			//case 5:  mood = "ƽ��"; break;
			//case 6:  mood = "�Ϻ�"; break;
			//case 7:  mood = "���"; break;
			//case 8:  mood = "����"; break;
			//case 9:  mood = "����"; break;
			//case 10: mood = "���"; break;
		//}
		//return m;
	//}
	
	////ʱ�������
	//private class TimerListener implements ActionListener {
		//public void actionPerformed(ActionEvent e) {
			//expUp(1);
			//repaint();
		//}
	//}
	
	////����ͼ										������
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
	        //g.drawString("������" + getAge(), getWidth() / 15, getHeight() / 13 * 2);
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