import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Chess {
    public static final int DIAMETER = PanelBoard.SPAN - 2;
    private int col; //棋子在棋盘中的x索引
    private int row; //棋子在棋盘中的y索引
    private Color color;//颜色
    PanelBoard cb;

    public Chess(PanelBoard cb, int col, int row, Color color) {
        this.cb = cb;
        this.col = col;
        this.row = row;
        this.color = color;
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

    public void draw(Graphics g) {
        int xPos = col * cb.SPAN + cb.MARGIN;
        int yPos = row * cb.SPAN + cb.MARGIN;

        Graphics2D g2d = (Graphics2D) g;
        Color fillColor = color; // 使用棋子的颜色作为填充颜色

        g2d.setColor(fillColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

        Ellipse2D e = new Ellipse2D.Float(xPos - DIAMETER / 2, yPos - DIAMETER / 2, DIAMETER, DIAMETER);
        g2d.fill(e);
    }

}


