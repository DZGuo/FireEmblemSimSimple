package characters;

public class Pos {
	private int x;
	private int y;

	public void setx(final int x) {
		this.x = x;
	}
	public void sety(final int y) {
		this.y = y;
	}
	public int getx() {
		return this.x;
	}
	public int gety() {
		return this.y;
	}
	
    public Pos() {
    	this.x = -1;
    	this.y = -1;
    }
}