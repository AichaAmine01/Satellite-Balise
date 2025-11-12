package Mobi;

import java.awt.Color;

import src.nicellipse.component.NiRectangle;

public class MobiView extends NiRectangle implements MobilListener{

	private Mobi mobi;
	
	public MobiView(Mobi mobi) {
		this.mobi = mobi;
		this.setBackground(Color.red);
	}
	
	@Override
	public void MobilMoveEvent(MobiMoveEvent event) {
		Mobi source = (Mobi) event.getSource();
		int x = source.getX();
		int y = source.getY();
		
		int pw = this.getParent().getWidth();
		int w = this.getWidth();
		int maxX = pw - w;
		
		x = ((x % maxX) + maxX) % maxX;
		
		this.setLocation(x, y);
	}

	@Override
	public void MobiHandCheckRequestEvent(MobiMoveEvent event) {
		// TODO Auto-generated method stub
		
	}
}
