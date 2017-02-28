package util;

import java.io.BufferedReader;
import java.io.IOException;

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

    /**
     * 数据格式 label v1 v2 ....
     * @param train_file
     */
    public void loadData(String train_file){
        BufferedReader br = text_proc.readFile(train_file);
        BufferedReader br2 = text_proc.readFile(train_file);
        String line = "";
        int N = 0;      //样本数
        int dim = 0;

        try {
            line = br.readLine();
            dim = line.split("[, ]").length-1;
            N++;
            while ((line = br.readLine()) != null){
                if (line.trim().length() > 1){
                    N++;
                }
            }
            this.setN(N);
            this.setDim(dim+1);    //多出一个偏置项
            double[][] x = new double[N][dim+1];
            for (int d = 0; d < N; d++) {
                x[d][dim] = 1;
            }
            double[] y = new double[N+1];
            int i = 0;
            while ((line = br2.readLine())!=null){
                if (line.trim().length() <= 1){
                    continue;
                }
                String[] arr = line.split("[ ,]");
                y[i] = Integer.parseInt(arr[0]);
                for (int d = 0; d < dim; d++) {
                    x[i][d] = Double.parseDouble(arr[d+1]);
                }
                i++;
            }
            this.setX(x);
            this.setY(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据格式 label v1 v2 ....
     * @param train_file
     */
    public void loadDataSVM(String train_file){
        BufferedReader br = text_proc.readFile(train_file);
        BufferedReader br2 = text_proc.readFile(train_file);
        String line = "";
        int N = 0;      //样本数
        int dim = 0;

        try {
            line = br.readLine();
            dim = line.split("[, ]").length-1;
            N++;
            while ((line = br.readLine()) != null){
                if (line.trim().length() > 1){
                    N++;
                }
            }
            this.setN(N);
            this.setDim(dim+1);    //多出一个偏置项
            double[][] x = new double[N][dim+1];
            for (int d = 0; d < N; d++) {
                x[d][dim] = 1;
            }
            double[] y = new double[N+1];
            int i = 0;
            while ((line = br2.readLine())!=null){
                if (line.trim().length() <= 1){
                    continue;
                }
                String[] arr = line.split("[ ,]");
                y[i] = Integer.parseInt(arr[0]);
                for (int d = 0; d < dim; d++) {
                    x[i][d] = Double.parseDouble(arr[d+1]);
                }
                i++;
            }
            this.setX(x);
            this.setY(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
