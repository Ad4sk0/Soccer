package soccer.utils;

public class Vector2d {

    private double x;
    private double y;

    public Vector2d() {
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2d multiply(Vector2d v1, Vector2d v2) {
        return new Vector2d(v1.getX() * v2.getX(), v1.getY() * v2.getY());
    }

    public static Vector2d multiply(Vector2d v, double scalar) {
        return new Vector2d(v.getX() * scalar, v.getY() * scalar);
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public void normalize() {
        if (length() == 0) return;
        double length = length();
        this.x /= length;
        this.y /= length;
    }

    public void reverseX() {
        x *= -1;
    }

    public void reverseY() {
        y *= -1;
    }

    public void setTo0() {
        x = 0;
        y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vector2d [x=" + x + ", y=" + y + "]";
    }
}
