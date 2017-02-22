package util;

/**
 * Created by yuanzhuo on 2017/2/22.
 */
public class model {
    private double weight[];    //模型权重
    private double acc;         //准去率
    private double recall;      //召回率

    public double[] getWeight() {
        return weight;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public double getAcc() {
        return acc;
    }

    public void setAcc(double acc) {
        this.acc = acc;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }


}
