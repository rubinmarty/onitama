
public class Vector {
	private int x;
	private int y;
	
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Vector flip() {
		return new Vector(-x, -y);
	}
	
	public Vector rotate(String s) { 
		switch (s) {
			case "CW":
				return new Vector(y,-x);
			case "CCW":
				return new Vector(-y,x);
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public Vector clone() {
		return new Vector(this.getX(), this.getY());
	}
	
	public Vector add(Vector v) {
		return new Vector(this.getX() + v.getX(), this.getY() + v.getY());
	}
	
	public Vector subtract(Vector v) {
		return this.add(v.flip());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		if (x != other.getX() || y != other.getY())
			return false;
		return true;
	}
}
