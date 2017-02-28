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
     * 数据格式 label indexi:vi  ....
     * @param train_file
     */
    public void loadDataSVM(String train_file){
        BufferedReader br = text_proc.readFile(train_file);
        BufferedReader br2 = text_proc.readFile(train_file);
        String line = "";
        int N = 0;      //样本数

        try {
//            line = br.readLine();
//            dim = line.split("[, ]").length-1;
//            N++;
            int max_dim = 0;
            while ((line = br.readLine()) != null){
                if (line.trim().length() <= 1){
                    continue;
                }
                String arr[] = line.split("[ \t]");
                for (String e: arr){
                    String e_arr[] = e.split(":");
                    if (e_arr.length < 2)
                        continue;
                    int index = Integer.parseInt(e_arr[0]);
                    if (max_dim < index){
                        max_dim = index;
                    }
                }
                N++;
            }
            this.setN(N);
            this.setDim(max_dim + 1);    //多出一个偏置项
            System.out.println("特征维数+1:" + dim);
            System.out.println("样本数:" + N);

            double[][] x = new double[N][dim];
            for (int d = 0; d < N; d++) {
                x[d][dim-1] = 1;
            }
            double[] y = new double[N];
            int i = 0;
            while ((line = br2.readLine())!=null){
                if (line.trim().length() <= 1){
                    continue;
                }
                String[] arr = line.split("[ ,\t]");
                if (arr[0].equals("+1")){
                    y[i] = 1;
                }else if(arr[0].equals("-1")){
                    y[i] = 0;
                }else{
                    y[i] = Integer.parseInt(arr[0]);
                }

                for (String f : arr) {
                    String f_arr[] = f.split(":");
                    if (f_arr.length < 2)
                        continue;
                    int index = Integer.parseInt(f_arr[0]);
                    double val = Double.parseDouble(f_arr[1]);
                    if (f_arr.length < 2)
                        continue;
                    x[i][index] = val;
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
