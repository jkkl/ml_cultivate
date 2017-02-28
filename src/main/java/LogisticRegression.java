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
            System.out.println("iter:"+it);
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

    public static void main(String[] args) {
        //String train_file = args[0];

        //iris
        String train_file = ".\\src\\main\\java\\data\\2_iris_train.txt";
        sample s = new sample();
        s.loadData(train_file);
        model m = new model();
        m.setWeight(new double[s.getDim()]);
        double rate = 10000;
        //trainSGD(s, m, rate, 1000, 1);

        // heart scale
        sample s_test = new sample();
        String test_file = ".\\src\\main\\java\\data\\heart_scale.txt";
        s_test.loadDataSVM(test_file);
        model m_heart = new model();
        m_heart.setWeight(new double[s_test.getDim()]);
        trainSGD(s_test,m_heart,0.1,100000,1);
        //double acc_test = acc(s_test,m);
        //System.out.println("acc_test:"+acc_test);


    }

}
