import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Chess {
	public static final int DIAMETER=PanelBoard.SPAN-2;  
	private int col; //�����������е�x����  
	private int row; //�����������е�y����  
	private Color color;//��ɫ  
	PanelBoard cb;

	public Chess(PanelBoard cb,int col,int row,Color color){  
		this.cb = cb;
		this.col=col;  
		this.row=row;  
		this.color=color;  
	}   

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public Color getColor() {
		return color;
	}

	public void draw(Graphics g){
		int xPos= col * cb.SPAN + cb.MARGIN;  
		int yPos= row * cb.SPAN + cb.MARGIN;  

		//Color colorOld = g.getColor();
		//g.setColor(color);//������ɫ  

		Graphics2D  g2d = (Graphics2D) g;
		RadialGradientPaint paint=null;
		int x = xPos + DIAMETER/4;
		int y = yPos - DIAMETER/4;
		float[] f = {0f, 1f};
		Color[] c = {Color.WHITE, Color.BLACK};
		if(color==Color.black){  
			paint = new RadialGradientPaint(x,y, DIAMETER, f, c );  
		}  
		else if(color==Color.white){  
			paint = new RadialGradientPaint(x, y, DIAMETER*2, f, c);  

		}  
		g2d.setPaint(paint); 
		//��������ʹ�߽������
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);  

		Ellipse2D e = new Ellipse2D.Float(xPos-DIAMETER/2, yPos-DIAMETER/2, DIAMETER, DIAMETER);  
		g2d.fill(e); 
		//g.setColor(colorOld);
	}

}
