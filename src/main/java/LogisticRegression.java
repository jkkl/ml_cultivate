import util.model;
import util.sample;
import util.text_proc;
import util.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yuanzhuo on 2017/2/21.
 */
public class LogisticRegression {

    private double      rate;   //学习率
    private int N;   //样本数
    private int opt;    //优化方法选择

    /**
     * 模型训练
     * @param X
     * @param Y
     * @param weigth
     * @param rate
     * @param iter
     * @param opt
     */
    public static void trainSGD(
            double[][] X,
            double[] Y,
            double[] weigth,
            double rate,
            int iter,
            int opt
    ){
        double N = Y.length;
        double dim = X[0].length;
        for (int it = 0; it < iter; it++){
            double logLikelihood = 0.0;
            for (int i = 0; i < N; i++) {
                double label = Y[i];
                double predict = predict(X[i], weigth);
                for (int d = 0; d < dim; d++) {
                    weigth[d] = weigth[d] - rate * (label - predict) * X[i][d]/N;
                }
                logLikelihood += label*Math.log(predict) + (1-label)*Math.log(1-predict);
            }
            System.out.println("iter "+it+" logLikelihood:" + logLikelihood);
            //System.out.println("acc "+acc(s.m));
        }
    }

    /**
     * 模型训练
     * @param s
     * @param m
     * @param rate
     * @param iter
     * @param opt
     * ！！！当目标函数是极大似然估计，要最大化目标函数时，用的是梯度上升，就沿梯度正方向
     * ！！！当目标函数是最小二乘，要最小化误差时，用的是梯度下降法，就沿梯度的负方向
     * 但然，为了统一用梯度下降法，可以在极大似然函数前加一个符号，转为就极小就可以了。
     */
    public static void trainSGD(
           sample s,
            model m,
            double rate,
            int iter,
            int opt
    ){
        double N = s.getN();
        double dim = s.getDim();
        for (int it = 0; it < iter; it++){
            double negLogLikelihood = 0.0;
            for (int i = 0; i < N; i++) {
                double label = s.getY()[i];
                double predict = predict(s.getX()[i], m.getWeight());
                for (int d = 0; d < dim; d++) {
                    m.getWeight()[d] = m.getWeight()[d] - rate * (predict - label) * s.getX()[i][d];//没弄明白最大时用梯度上升，极小时用梯度下降
                }
                negLogLikelihood += -(label*Math.log(predict) + (1-label)*Math.log(1-predict));//当预测值与label无限接近时，预测值为1，导致求0的对数，输出NaN
            }
            util.printVector(m.getWeight());
            System.out.println("acc " + acc(s, m));
            System.out.println("negLogLikelihood "+negLogLikelihood);
        }
    }

    public static double acc(sample s, model m){
        double acc = 0.0;
        for (int i = 0; i < s.getN(); i++) {
            double predict = predict(s.getX()[i],m.getWeight());
            if ( Math.abs(s.getY()[i] - predict) < 0.5 ){
                acc += 1;
            }
        }
        return acc/s.getN();
    }

    /**
     * 类别预测函数
     * @param x
     * @param w
     * @return
     */
    public static double predict(double[] x, double[] w){
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            result += x[i]*w[i];
        }
        return sigmoid(result);
    }

    /**
     * sigmoid 函数
     * @param x
     * @return
     */
    public static double sigmoid(double x){
        return 1.0/(1.0 + Math.exp(-x));    //!!! 写成了，Math.log()
    }

    public static void loadData(String train_file,double[][] X,double[] Y){
        BufferedReader br = text_proc.readFile(train_file);
        BufferedReader br2 = text_proc.readFile(train_file);
        String line = "";
        int N = 0;      //样本数
        int dim = 0;

        try {
            line = br.readLine();
            dim = line.split("[, ]").length-1;
            while ((line = br.readLine()) != null){
                if (line.trim().length() > 1){
                    N++;
                }
            }
            double[][] x = new double[N][dim];
            double[] y = new double[N];
            int i = 0;
            while ((line = br2.readLine())!=null){
                if (line.trim().length() < 1){
                    continue;
                }
                String[] arr = line.split("[ ,]");
                y[i] = Integer.parseInt(arr[0]);
                for (int d = 0; d < dim; d++) {
                    x[i][d] = Double.parseDouble(arr[d+1]);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(String train_file, sample s){
        BufferedReader br = text_proc.readFile(train_file);
        BufferedReader br2 = text_proc.readFile(train_file);
        String line = "";
        int N = 0;      //样本数
        int dim = 0;

        try {
            line = br.readLine();
            dim = line.split("[, ]").length-1;
            while ((line = br.readLine()) != null){
                if (line.trim().length() > 1){
                    N++;
                }
            }
            s.setN(N+1);
            s.setDim(dim);
            double[][] x = new double[N+1][dim];
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
            s.setX(x);
            s.setY(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        //String train_file = args[0];
        String train_file = ".\\src\\main\\java\\data\\iris_f.txt";
        sample s = new sample();
        loadData(train_file,s);
        model m = new model();
        m.setWeight(new double[s.getDim()]);
        double rate = 1000000;
        trainSGD(s,m,rate,10,1);
    }

}
