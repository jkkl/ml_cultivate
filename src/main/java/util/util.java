package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by yuanzhuo on 2017/2/21.
 */
public class util {
    /**
     * 填充嵌套hashmap Map<String,Map<String,Double>>
     * @param key1  外层关键词
     * @param key2  内层关键词
     * @param val   内层值
     * @param hm_source 要填充的hashmap
     */
    public static void fillHM(String key1,String key2,Double val,Map<String,Map<String,Double>> hm_source ){
        if (hm_source.containsKey(key1)){
            Map<String,Double> hm_label_idf = hm_source.get(key1);
            if (hm_label_idf.containsKey(key2)){
                hm_label_idf.put(key2,hm_label_idf.get(key2)+val);
            }else {
                hm_label_idf.put(key2,val);
            }
        }else {
            Map<String,Double> hm_label_idf = new HashMap<String, Double>();
            hm_label_idf.put(key2,val);
            hm_source.put(key1,hm_label_idf);
        }
    }

    /**
     * 填充嵌套hashmap Map<String,Map<String,Double>>
     * @param key  关键词
     * @param val   值
     * @param hm_source 要填充的hashmap
     */
    public static <K,V> void fillHM(K key,V val,Map<K,V> hm_source ){
        if (hm_source.containsKey(key)){
            //hm_source.put(key,hm_source.get(key) + val);
            return;
        }else {
            hm_source.put(key,val);
        }
    }

    public static String  sortHashMap(Map<String,Double> source) {
        StringBuffer sb = new StringBuffer();
        List<Map.Entry<String, Double>> sort_list;

        sort_list = new ArrayList<Map.Entry<String, Double>>(source.entrySet());

        Collections.sort(sort_list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for(int i = 0; i < sort_list.size(); i++){
            Map.Entry<String, Double> en =sort_list.get(i);
            sb.append(en.getKey());
            sb.append(":");
            sb.append(en.getValue());
            sb.append(" ");
        }
        return  sort_list.toString();
    }


    public static String  sortHashMapA(Map<Integer,Double> source){
        StringBuffer sb = new StringBuffer();
        List<Map.Entry<Integer,Double>> sort_list;

        sort_list = new ArrayList<Map.Entry<Integer, Double>>(source.entrySet());

        Collections.sort(sort_list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                if (o1.getKey() > o2.getKey()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for(int i = 0; i < sort_list.size(); i++){
            Map.Entry<Integer, Double> en =sort_list.get(i);
            sb.append(en.getKey());
            sb.append(":");
            sb.append(en.getValue());
            sb.append(" ");
        }
        return  sb.toString();
    }

    public static List<Map.Entry<String,Double>>  sortHashMapA(Map<String,Double> source){

        //StringBuffer sb = new StringBuffer();
        List<Map.Entry<String,Double>> sort_list;

        sort_list = new ArrayList<Map.Entry<String, Double>>(source.entrySet());

        Collections.sort(sort_list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return  sort_list;
    }

    public static List<Map.Entry<String,Integer>>  sortHashMapB(Map<String,Integer> source){

        //StringBuffer sb = new StringBuffer();
        List<Map.Entry<String,Integer>> sort_list;

        sort_list = new ArrayList<Map.Entry<String, Integer>>(source.entrySet());

        Collections.sort(sort_list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return  sort_list;
    }

    public static void swap(List<String> arr, int sour, int dest){
        String tmp = arr.get(sour);
        arr.set(sour,arr.get(dest));
        arr.set(dest,tmp);
        return;
    }

    /**
     * 多分类，标签为one-hot
     * @param file_path
     * @param sample
     * @param label
     * @throws IOException
     */
    public static void fillMatrixTest(String file_path,Vector<Double[]> sample,Vector<Double[]> label,int output_node_num) throws IOException{
        BufferedReader br = text_proc.readFile(file_path);
        String line;
        while ((line = br.readLine()) != null){
            String line_arr[] = line.split("\t");
            if (line_arr.length != 2){
                System.err.print("sample format error");
                return;
            }
            //添加标签，one hot 形式
            int y = Integer.parseInt(line_arr[0]);

            Double[] v_y = new Double[output_node_num];
            if (output_node_num == 1){
                v_y[0] = 1.0*y;
            }else{
                for (int i=0; i<output_node_num;i++){
                    if (i == y){
                        v_y[i] = 1.0;
                    }else {
                        v_y[i] = 0.0;
                    }
                }
            }

            label.add(v_y);

            //添加特征
            String x[] = line_arr[1].split("[, ]");
            Double v_x[] = new Double[x.length];
            for (int i = 0 ; i<x.length; i++){
                v_x[i] = Double.parseDouble(x[i]);
            }
            sample.add(v_x);
        }
    }

    /**
     * 多分类，标签为one-hot
     * @param file_path
     * @param sample
     * @param label
     * @throws IOException
     */
    public static void fillMatrix(String file_path,Vector<Double[]> sample,Vector<Double[]> label,int output_node_num) throws IOException{
        BufferedReader br = text_proc.readFile(file_path);
        String line;
        while ((line = br.readLine()) != null){
            String line_arr[] = line.split("\t");
            if (line_arr.length != 2){
                System.err.print("sample format error");
                return;
            }
            //添加标签，one hot 形式
            int y = Integer.parseInt(line_arr[0]);

            Double[] v_y = new Double[output_node_num];
            if (output_node_num == 1){
                v_y[0] = 1.0*y;
            }else{
                for (int i=0; i<output_node_num;i++){
                    if (i == y){
                        v_y[i] = 1.0;
                    }else {
                        v_y[i] = 0.0;
                    }
                }
            }

            label.add(v_y);

            //添加特征
            String x[] = line_arr[1].split("[, ]");
            Double v_x[] = new Double[x.length];
            for (int i = 0 ; i<x.length; i++){
                v_x[i] = Double.parseDouble(x[i]);
            }
            sample.add(v_x);
        }
    }


    /**
     * 打印矩阵
     * @param m
     */
    public static void printMatrix(Vector<Double[]> m){
        if (m == null){
            return;
        }
        for (Double[] e : m){
            for (Double ee : e){
                System.out.print(ee+" ");
            }
            System.out.print("\n");
        }
    }

    /**
     * 打印矩阵
     * @param m
     */
    public static void printVector(Vector<Double> m){
        if (m == null){
            return;
        }
        for (Double e : m){
            System.out.print(e);
            System.out.print("\n");
        }
    }

    public static void printVector(double[] m){
        if (m == null){
            return;
        }
        for (Double e : m){
            System.out.print(e+" ");

        }
        System.out.print("\n");
    }

    public static void initVector(Vector<Double> v,int size,Double value){
        if (v == null ){
            System.err.print("v is null!");
            return;
        }
        for (int i=0; i<size; i++){
            v.add(i,value);
        }
    }
    /**
     * 随机初始化矩阵个元素的值
     * @param m
     * @param row
     * @param col
     */
    public static void randomMatrix(Vector<Double[]> m,int row, int col){
        if (m == null ){
            System.err.print("matrix is empty!");
            return;
        }
        for (int i=0; i<row; i++){
            Double[] e = new Double[col];
            for (int j =0; j<col; j++){
                e[j] = Math.random()*(Math.random()>=0.5?1:-1)/10.0;
            }
            m.add(e);
        }

    }


    /**
     * 两个相加
     * @param a
     * @param b
     * @return
     */
    public static Vector<Double> vectorAdd(Vector<Double> a,Vector<Double> b){
        Vector<Double> result = new Vector<Double>();
        for (int i =0; i<a.size(); i++){
            Double e = a.get(i)+b.get(i);
            result.add(e);
        }
        return result;
    }

    public static Double[] vectorAdd(Double[] a,Double[] b){
        Double[] result = new Double[a.length];
        for (int i =0; i<a.length; i++){
            result[i] = a[i] +b[i];
        }
        return result;
    }

    /**
     * 两个矩阵相加
     * @param a
     * @param b
     * @return
     */
    public static Vector<Double[]> matrixAdd(Vector<Double[]> a,Vector<Double[]> b){
        Vector<Double[]> result = new Vector<Double[]>();
        int row = a.size();
        int col = a.get(0).length;
        if (b.size() != row || b.get(0).length != col){
            System.err.println("Matrix shape no match");
            return null;
        }
        for (int i =0; i<a.size(); i++){

            Double[] e_new = new Double[col];
            for (int j = 0 ; j<a.get(i).length; j++){
                e_new[j] = a.get(i)[j]+b.get(i)[j];
            }
            result.add(e_new);
        }

        return result;
    }
    /**
     * 两个矩阵相减 A-B
     * @param a
     * @param b
     * @return
     */
    public static Vector<Double[]> matrixSub(Vector<Double[]> a,Vector<Double[]> b){
        Vector<Double[]> result = new Vector<Double[]>();
        int row = a.size();
        int col = a.get(0).length;
        if (b.size() != row || b.get(0).length != col){
            System.err.println("Matrix shape no match");
            return null;
        }
        for (int i =0; i<a.size(); i++){

            Double[] e_new = new Double[col];
            for (int j = 0 ; j<a.get(i).length; j++){
                e_new[j] = a.get(i)[j]-b.get(i)[j];
            }
            result.add(e_new);
        }

        return result;
    }


    /**
     * 求矩阵*系数
     * @param m
     * @return
     */
    public static Vector<Double[]> matrixMultipyConstant(Vector<Double[]> m,
                                                         Double c){
        if (m == null)
            return null;
        int col = m.get(0).length;
        Vector<Double[]> result = new Vector<Double[]>();
        for (Double[] e : m){
            Double[] e_new = new Double[col];
            for (int i = 0 ; i<e.length ;i++){
                e_new[i] = e[i] * c;
            }
            result.add(e_new);
        }
        return result;
    }

    /**
     * 求矩阵*系数
     * @param m
     * @return
     */
    public static Vector<Double> vectorMultipyConstant(Vector<Double> m,
                                                       Double c){
        if (m == null)
            return null;
        Vector<Double> result = new Vector<Double>();
        for (Double e : m){
            Double e_new = e * c;
            result.add(e_new);
        }
        return result;
    }

    public static Double[] vectorMultipyConstant(Double[] m,
                                                 Double c){
        if (m == null)
            return null;
        Double[] result = new Double[m.length];
        for (int i = 0; i<m.length; i++){
            result[i] = m[i] * c;
        }
        return result;
    }

    /**
     * 矩阵按列求和，结果为一行n列的向量
     * @param m
     * @return
     */
    public static Vector<Double> matrixSumColumn(Vector<Double[]> m){
        Vector<Double> sum = new Vector<Double>();
        if (m == null || m.size() < 1){
            System.err.print("Empty maxtix");
            return null;
        }
        int col_num = m.get(0).length;  //
        for (int i=0; i<col_num; i++){
            Double sum_col = 0.0;
            for (Double[] e : m){
                sum_col += e[i];
            }
            sum.add(sum_col);
        }
        return sum;
    }

    public static void verifyDerivative(){

    }

    /**
     * 求某一隐层所有节点的权重的偏导
     * @param a
     * @param residual_next_layer_all
     * @return
     */
    public static Vector<Double[]> derivative(Vector<Double[]> a,   //第l层的所有样本的激活值，每个数组为一个样本
                                              Vector<Double[]> w,   //第l层到第l+1层的连接权重
                                              Vector<Double[]> residual_next_layer_all, //第l+1层，所有样本的残差，每个数组元素一个一样本
                                              int sample_num,      //样本总数
                                              Double reg_lambda
    ){
        Vector<Double[]> a_T = T(a);    //转置后，每一行元素为所有样本，在第l层，第i个节点的激活值
        Vector<Double[]> residual_next_layer_all_T = T(residual_next_layer_all);//转置后，每一行元素，为所有样本在l+1层第i个节点的残差


        //所有样本的第l层各个节点，到l+1层各节点连接权重的导数的和
        Vector<Double[]> dw = matrixProduct(a_T,residual_next_layer_all_T);     //每一行元素为第l层中节点i到l+1层各节点的权重导数
        //连接导数求均值并加上罚项
        return matrixAdd(matrixMultipyConstant(dw, 1.0 / sample_num), matrixMultipyConstant(w, reg_lambda));
        //return matrixMultipyConstant(dw, 1.0 / sample_num);

    }

    /**
     * 求某一隐层所有节点的权重的偏导
     * @param a
     * @param residual_next_layer_all
     * @return
     */
    public static Vector<Double[]> derivative(Vector<Double[]> a,   //第l层的所有样本的激活值，每个数组为一个样本
                                              Vector<Double[]> w,   //第l层到第l+1层的连接权重
                                              Vector<Double[]> residual_next_layer_all, //第l+1层，所有样本的残差，每个数组元素一个一样本
                                              int sample_num      //样本总数
                                              //Double reg_lambda
    ){
        Vector<Double[]> a_T = T(a);    //转置后，每一行元素为所有样本，在第l层，第i个节点的激活值
        Vector<Double[]> residual_next_layer_all_T = T(residual_next_layer_all);//转置后，每一行元素，为所有样本在l+1层第i个节点的残差


        //所有样本的第l层各个节点，到l+1层各节点连接权重的导数的和 激活值*后一层的残差
        Vector<Double[]> dw = matrixProduct(a_T,residual_next_layer_all_T);     //每一行元素为第l层中节点i到l+1层各节点的权重导数
        //连接导数求均值并加上罚项
        return matrixMultipyConstant(dw, 1.0 / sample_num);


    }

    public static Vector<Double> biasDerivative(Vector<Double[]> residual_next_layer_all,
                                                int sample_num    //样本总数
    ){

        return vectorMultipyConstant(matrixSumColumn(residual_next_layer_all), 1.0 / sample_num);
    }

    public static Vector<Double> biasDerivative(Vector<Double[]> residual_next_layer_all
    ){
        //偏置项的偏导 = 后一层的残差
        return matrixSumColumn(residual_next_layer_all);
    }


    /**
     * 计算所有样本在某一隐层上各个节点的残差
     * @param w
     * @param active_all
     * @param residual_next_all
     * @return
     */

    public static Vector<Double[]> residualHideLayer(Vector<Double[]> w,    //第l层节点，到下一层节点的连接权重，一个数组元素代表第l层的一个节点
                                                     Vector<Double[]> active_all, // 第l层激活值表，每个数组元素为一个样本
                                                     Vector<Double[]> residual_next_all){    //第l+1层残差值表，每个数组元素为一个样本

        Vector<Double[]> residual_hide_all = new Vector<Double[]>();
        for (int i = 0 ; i < active_all.size(); i++){
            residual_hide_all.add(residualHideOneSample(w, active_all.get(i), residual_next_all.get(i)));
        }
        return residual_hide_all;
    }

    /**
     * 计算单一样本，某一隐层各个节点的残差
     * @param w
     * @param active
     * @param residual_next_layer
     * @return
     */
    public static Double[] residualHideOneSample(Vector<Double[]> w,    //第l层各节点，到l+1层的权重，每个数组元素代表一个节点
                                                 Double[] active,       //第l层各个节点的激活值
                                                 Double[] residual_next_layer){ //第l+1层各个节点的残差

        Double[] residual_layer = new Double[w.size()];
        for (int i = 0; i < w.size(); i++){
            residual_layer[i] = residualHideNode(w.get(i), active[i], residual_next_layer);
        }
        return residual_layer;
    }

    /**
     * 计算隐层中，某一节点的残差
     * @param w
     * @param active
     * @param residual_next_layer
     * @return
     */
    public static Double residualHideNode(Double[] w,   //该节点到下一层各节点的连接权重
                                          Double active,    //该节点的激活值
                                          Double[] residual_next_layer  //下一层各节点的残差
    ){
        Double residual;
        int next_layer_node_num = residual_next_layer.length;
        if (w.length != next_layer_node_num){
            System.err.print("the length of w and next layer is not same!");
        }
        Double weighted_residual = 0.0;
        for (int i =0 ; i<next_layer_node_num;i++){
            weighted_residual += w[i]*residual_next_layer[i];
        }
        residual = weighted_residual*active*(1-active);
        return residual;
    }

    /**
     * 计算输出层残差 (yi-ai)*ai*(ai-1)
     * @param probs
     * @param label
     * @return
     */
    public static Vector<Double[]> residualOutLayer(Vector<Double[]> probs,Vector<Double[]> label){
        Vector<Double[]> residual = new Vector<Double[]>();
        int out_num = label.size();
        if (probs.size() != out_num){
            System.err.print("Different size of probs and label");
        }

        for (int i =0; i < out_num; i++){
            residual.add(residualOutLayerOne(probs.get(i),label.get(i)));
        }
        return residual;
    }

    /**
     * 计算单一样本输出层残差 (yi-ai)*ai*(ai-1)
     * @param probs
     * @param label
     * @return
     */
    public static Double[] residualOutLayerOne(Double[] probs,Double[] label){
        Double[] residual = new Double[label.length];
        int out_num = label.length;
        if (probs.length != out_num){
            System.err.print("Different size of probs and label");
        }

        for (int i =0; i < out_num; i++){
            residual[i] = -1*(label[i]-probs[i])*probs[i]*(1-probs[i]);
        }
        return residual;
    }

    /**
     * 求矩阵的转置，把原来二维数组的列传为新数组的行
     * @param m
     * @return
     */
    public static Vector<Double[]> T(Vector<Double[]> m){
        Vector<Double[]> result = new Vector<Double[]>();
        if (m.size()<1){
            System.err.print("Empty matrix!");
        }
        int col = m.get(0).length;
        int row = m.size();
        for (int i=0; i < col ; i++){
            Double[] new_row = new Double[row];
            for (int j=0; j<row; j++){
                new_row[j] = m.get(j)[i];
            }
            result.add(new_row);
        }
        return result;
    }




    /**
     *  预测样本集合所有元素，输出元素预测类别
     * @param
     * @param
     * @return
     */
    public static Double accuracy (Vector<Double> predict,
                                   Vector<Double[]> y){

        int size = predict.size();
        if (size < 1){
            return null;
        }
        if (size != y.size()){
            System.err.print("predict and y size is not same");
            return null;
        }


        int outnode_num = y.get(0).length;
        //输出节点大于一
        int correct_num = 0;
        for (int i = 0; i<size; i++){
            //输出节点数为一
            if (outnode_num == 1){

                if (y.get(i)[0].equals(predict.get(i))){
                    correct_num++;
                }
            }else{
                if (y.get(i)[predict.get(i).intValue()] > 0.5){
                    correct_num++;
                }
            }

        }

        //返回概率最高的类别
        //System.out.print(correct_num);
        return 1.0*correct_num/size;
    }


    /**
     * 计算样本集中每个元素所属类别
     * @param probs
     * @return
     */
    public static Vector<Double> outLable(Vector<Double[]> probs){
        Vector<Double> result = new Vector<Double>();
        for (Double[] e : probs){
            result.add(outLableOne(e));
        }
        return result;
    }

    /**
     * 计算一个样本所属类别：比较输出层各节点输出概率的最大值，返回该值所对应节点下标(序号)
     * @param probs
     * @return
     */
    public static Double outLableOne(Double[] probs){
        //输出节点为一时，二分类，当输出值大于0.5输出1否则为0
        if (probs.length == 1){
            if (probs[0] > 0.5){
                return 1.0;
            }else {
                return 0.0;
            }
        }

        //当输出节点大于一时，多分类，返回输出值最大的那个节点的序号
        int label_one = -1;
        Double tmp = 0.0;
        for (int i=0; i<probs.length; i++){
            if (probs[i]>tmp){
                tmp = probs[i];
                label_one = i;
            }
        }
        return (double)label_one;
    }



    /**
     * 计算矩阵的L2正则
     * @param w
     * @return
     */
    public static Double L2(Vector<Double[]> w){
        Double result = 0.0;
        for (Double[] e_arr : w){
            for (Double e : e_arr){
                result += e*e;
            }
        }
        return result;
    }

    /**
     * Vector元素求和
     * @param v
     * @return
     */
    public static Double addVector(Vector<Double> v){
        Double result = 0.0;
        for (Double e : v){
            result += e;
        }

        return result;
    }

    /**
     * 计算输出层的负对数, 对于多分类，只计算其正确类别
     * @param probs
     * @param label
     * @return
     */
    public static Vector logProbs(Vector<Double[]> probs,Vector<Double[]> label){
        Vector<Double> result = new Vector<Double>();
        for (int i = 0; i<probs.size(); i++){
            Double[] e = probs.get(i);

            Double normalize = 0.0;
            for (Double ee : e){
                normalize+=ee;
            }
            for (int j = 0 ; j < e.length; j++){
                e[j] = e[j]/normalize;
            }


            if (e.length == 1){//输出层节点数为1
                result.add(-Math.log(e[0]*1.0));
            }else {//输出层节点大于1
                int class_k = getPosLabel(label.get(i));
                result.add(-Math.log(e[class_k]*1.0));
            }

        }
        return result;
    }

    /**
     * 获取该标签所属类别，即正类在标签列表中的下标
     * @param label
     * @return
     */
    public static int getPosLabel(Double[] label){

        if (label == null)
            return -1;
        int index = 0;
        for (; index <label.length; index++){
            if (label[index] > 0.5){
                break;
            }
        }
        if (index == label.length){
            return -1;
        }
        return index;
    }

    /**
     * 求两个个向量的内积
     * @param a
     * @param b
     * @return
     */

    public static Double vecProduct(Double[] a, Double[] b){
        Double result = 0.0;
        int vec_size = a.length;
        if ( vec_size < 1 || vec_size != b.length){
            System.err.print("vecProduct the size is  Incompatible");
            return null;
        }

        for (int i = 0 ; i < vec_size; i++){
            result += a[i] * b[i];
        }

        return result;
    }

    public static Vector<Double[]> inputNextLayer(Vector<Double[]> feature,Vector<Double[]> weight,Vector<Double> bias){
        Vector<Double[]> result = new Vector<Double[]>();
        Vector<Double[]> weight_T = T(weight);
        int col = weight_T.size();
        for (int i = 0 ; i < feature.size(); i++){
            Double[] z = new Double[col];
            for (int j = 0; j<weight_T.size(); j++){
                z[j] = vecProduct(feature.get(i),weight_T.get(j))+bias.get(j);
            }
            result.add(z);
        }
        return result;
    }

    /**
     * 两个向量相乘
     * @param a
     * @param b
     * @return
     */

    public static Double vectorProduct(Double[] a,Double[] b){
        Double result = 0.0;
        int col = a.length;
        if (a.length != b.length){
            System.err.print("vectorProduct : Vector size not equal");
            return null;
        }
        for (int i = 0; i < col; i++){
            result += a[i]*b[i];
        }
        return result;
    }

    /**
     * 两个矩阵相乘 a 按行存储  b 把列转为行，再存储
     * @param a
     * @param b
     * @return
     */

    public static Vector matrixProduct(Vector<Double[]> a,Vector<Double[]> b){
        Vector v_result = new Vector();
        int z_num = b.size();

        for (int i = 0; i < a.size(); i++){
            Double[] row_vec = a.get(i);
            Double[] Z = new Double[z_num];
            for (int j = 0; j < b.size(); j++){
                Double[] col_vec = b.get(j);
                Double zj = vecProduct(row_vec,col_vec);
                Z[j] = zj;
            }
            v_result.add(Z);
        }
        return v_result;
    }

    /**
     * 使用双曲正切计算单一神经元的激活值
     * @param z
     * @return
     */
    public static Double calActiveOne(Double z){
        //return  (Double)Math.tanh((double) z);
        return  sigmoid(z);
    }



    /**
     * 使用双曲正切计算一个样本在一层中每个神经元的激活值
     * @param z 一个样本在各个神经元的输入
     *
     * @return
     */
    public static Double[] calActiveLevel(Double[] z){
        Double r[] = new Double[z.length];
        for (int i=0; i< z.length; i++){
            r[i] = calActiveOne(z[i]);
        }
        return  r;
    }

    /**
     * 使用双曲正切计算所有样本在一层中每个神经元的激活值
     * @param z 一个数组为一个样本在该层所有神经元的输入
     *
     * @return
     */
    public static Vector calActive(Vector<Double[]> z){
        Vector v_r = new Vector();

        //单个样本激活值
        for(int i = 0; i<z.size(); i++){
            Double[] active_layer = calActiveLevel(z.get(i));
            v_r.add(active_layer);
        }
        return  v_r;
    }

    /**
     * sigmoid 函数，计算输出概率
     * @param x
     * @return
     */
    public static Double sigmoid(Double x){
        return ( 1.0 / (1.0 + Math.exp(-x)) );
    }

    /**
     * 计算一个样本输出层的输出概率，先计算输出值，然后归一化
     * @param z
     * @return
     */
    public static Double[] calOutputOneSample(Double[] z){
        Double[] result = new Double[z.length];
        Double normalization = 0.0 ;
        for (int i = 0 ; i < z.length; i++){
            result[i] = sigmoid(z[i]);
            normalization += result[i];
        }
        for (int i = 0 ; i < z.length; i++){
            //result[i] = result[i]/normalization;
        }

        return result;
    }


    /**
     * 计算训练集中每个样本为各个类别的概率
     * @param z
     * @return
     */
    public static Vector<Double[]> calOutput(Vector<Double[]> z){
        Vector r = new Vector();
        for (Double[] e : z){
            r.add(calOutputOneSample(e));
        }
        return r;
    }
}
