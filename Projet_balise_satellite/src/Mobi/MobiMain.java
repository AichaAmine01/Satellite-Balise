package Mobi;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import src.nicellipse.component.NiRectangle;
import src.nicellipse.component.NiSpace;

	public class MobiMain {
		
		public static void main(String[] args) throws InterruptedException {
			NiSpace space = new NiSpace("Mobi", new Dimension (400, 400));
			space.setBackground(Color.gray);
			
			Mobi mobi1 = new Mobi(20,20, 1);
			MobiView mobiView1 = new MobiView(mobi1);
			
			mobi1.registerMoveEvent(mobiView1);
			
			Mobi mobi12 = new Mobi(20,60, -1);
			MobiView mobiView2 = new MobiView(mobi1);
			
			mobi12.registerMoveEvent(mobiView2);
			
			space.add(mobiView1);
			space.add(mobiView2);
			space.openInWindow();
			while(true) {
				mobi1.move(3);
				mobi12.move(5);
				Thread.sleep(20);
			}
		}
		
	}

