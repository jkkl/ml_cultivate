package util;

/**
 * Created by yuanzhuo on 2017/2/22.
 */
public class sample {
    private int dim;
    private int N;
    private double[] Y;
    private double[][] X;

    public int getN() {
        return N;
    }
    public void setN(int n) {
        N = n;
    }
    public int getDim() {
        return dim;
    }
    public void setDim(int dim) {
        this.dim = dim;
    }
    public double[][] getX() {
        return X;
    }
    public void setX(double[][] x) {
        X = x;
    }
    public double[] getY() {
        return Y;
    }
    public void setY(double[] y) {
        Y = y;
    }
}
