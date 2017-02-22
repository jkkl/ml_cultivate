package util;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuanzhuo on 2017/2/21.
 */
public class text_proc {

        /**
         * 读取文件接口
         * @param in_file
         * @return
         * @throws IOException
         */
        public static BufferedReader readFile(String in_file) {
            BufferedReader br_input_file = null;
            try {
                br_input_file = new BufferedReader(new InputStreamReader(new FileInputStream(in_file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return br_input_file;
        }


        /**
         * 写文件接口
         * @param out_file
         * @return
         * @throws IOException
         */
        public static FileWriter writFile(String out_file){
            FileWriter fw_output = null;
            try {
                fw_output = new FileWriter(out_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fw_output;
        }


        /**
         * 将标注向量集合 按比例随机分为训练集 和 测试集
         * @param arr_sample
         * @param part_rate 测试集占比
         * @param out_train_file
         * @param out_test_file
         * @throws IOException
         */
        public static void randPartition(List<String> arr_sample,double part_rate,
                                         String out_train_file,String out_test_file) throws IOException{
            int total = arr_sample.size();
            if (total < 1)
                return;
            FileWriter fw_train = writFile(out_train_file);
            FileWriter fw_test = writFile(out_test_file);

            Random rd = new Random();
            long select_num = Math.round(total*part_rate);
            int index = -1;
            int i = 0;
            StringBuffer sb_out_train = new StringBuffer();
            StringBuffer sb_out_test = new StringBuffer();
            while (i != select_num){
                index = rd.nextInt(total - i);

                sb_out_test.append(arr_sample.get(index));
                sb_out_test.append("\n");

                util.swap(arr_sample, index, total - i -1);
                i++;
            }
            fw_test.write(sb_out_test.toString());
            fw_test.close();

            int train_num = total - i;
            while (train_num > 0){
                sb_out_train.append(arr_sample.get(train_num--));
                sb_out_train.append("\n");
            }
            fw_train.write(sb_out_train.toString());
            fw_train.close();
        }
        /**
         * 从样本文件中，随机抽样
         * @param in_sample_file
         * @param count     抽样占比，或条数
         * @param amount    记录总条数
         * @param out_sample_file
         * @throws IOException
         */
        public static void randSample(String in_sample_file, double count, double amount,String out_sample_file) throws IOException {
            if (count > amount || count <= 0 || amount < 1){
                return;
            }
            double scale = 0.0;
            if (amount >= 1){
                amount -= amount*2/3;
                scale = count / amount;
            }else {
                scale = count;
            }
            Math.random();
            BufferedReader br_input_file;
            br_input_file = new BufferedReader(new InputStreamReader(new FileInputStream(in_sample_file)));
            FileWriter fw_output = new FileWriter(out_sample_file);
            StringBuffer sb_out = new StringBuffer();
            String record = "";
            int out_num = 0;
            while ((record = br_input_file.readLine()) != null){

                if (Math.random() < scale){
                    if (record.startsWith("20000")){
                        if (Math.random() > 0.04){
                            continue;
                        }
                    }
                    fw_output.append(record);
                    fw_output.append("\n");

                    out_num++;
                    if (count >= 1){
                        if (out_num >= count){
                            break;
                        }
                    }
                }
            }

            fw_output.close();
        }

        /**
         * 从样本文件中，随机抽样
         * @param in_sample_file
         * @param count     抽样占比，或条数
         * @param out_sample_file
         * @throws IOException
         */
        public static void randSample(String in_sample_file, double count,String out_sample_file) throws IOException {
            BufferedReader br_tmp = readFile(in_sample_file);
            int amount = 0;
            while (br_tmp.readLine() != null){
                ++amount;
            }
            if (count > amount || count <= 0 || amount < 1){
                return;
            }
            double scale = 0.0;
            if (amount >= 1){
                amount -= amount*2/3;
                scale = count / amount;
            }else {
                scale = count;
            }
            Math.random();
            BufferedReader br_input_file;
            br_input_file = new BufferedReader(new InputStreamReader(new FileInputStream(in_sample_file)));
            FileWriter fw_output = new FileWriter(out_sample_file);
            StringBuffer sb_out = new StringBuffer();
            String record = "";
            int out_num = 0;
            while ((record = br_input_file.readLine()) != null){

                if (Math.random() < scale){
                    if (record.startsWith("20000") || record.startsWith("游戏")){
                        if (Math.random() > 0.05){
                            continue;
                        }
                    }else if (record.startsWith("10700") || record.startsWith("实用工具")){
                        if (Math.random() > 0.06){
                            continue;
                        }
                    }
                    fw_output.append(record);
                    fw_output.append("\n");

                    out_num++;
                    if (count >= 1){
                        if (out_num >= count){
                            break;
                        }
                    }
                }
            }

            fw_output.close();
        }

        /**
         * 从预测结果文件中，随机抽样检验
         * @param pre_sample_file   预测的样本文件
         * @param predicted_file    预测的结果文件：预测的类别，只有一列
         * @param dic_file           类别id到类别名的词典 col1：cat_name     col2:cat_id
         *
         * @param out_sample_file   输出的文件名
         * @throws IOException
         */
        public static void evaCatSample(String pre_sample_file,String predicted_file,String dic_file,String out_sample_file) throws IOException {


            BufferedReader rb_dim = readFile(dic_file);
            BufferedReader rb_sample = readFile(pre_sample_file);
            BufferedReader rb_pred = readFile(predicted_file);
            FileWriter fw_out = writFile(out_sample_file);
            String line;
            // 读取dim文件
            Map<String,String> cat_dim = new HashMap<String, String>();

            while ((line = rb_dim.readLine())!= null){
                String arr_line[] = line.split("\t");
                util.fillHM(arr_line[1],arr_line[0],cat_dim);
            }
            rb_dim.close();

//        int total_num = 0;
//        rb_sample.mark(rb_sample.toString().length() + 10);
//        rb_pred.mark(rb_pred.toString().length() + 10);
//        while (rb_sample.readLine() != null){
//            if (rb_pred.readLine() == null){
//                System.out.println("Error:样本文件和预测结果文件行数不一致");
//            }
//            total_num++;
//        }
//        rb_sample.reset();
//        rb_pred.reset();

            //读取样本文件和预测文件
            StringBuffer sb_out = new StringBuffer();
            int i_num = 0;
            while ((line = rb_pred.readLine()) != null){
                String name = cat_dim.get(line);
                sb_out.append(name);
                sb_out.append("\t");
                sb_out.append(rb_sample.readLine());
                sb_out.append("\n");

                if (++i_num % 10000 == 0) {
                    fw_out.write(sb_out.toString());
                    sb_out.setLength(0);
                }
            }

            fw_out.write(sb_out.toString());
            fw_out.close();
            rb_pred.close();
            rb_sample.close();

        }


        /*
         filter no Chinese
         */
        public static void filter(String input,String output) throws IOException {
            BufferedReader br_input_file;

            br_input_file = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
            FileWriter fw_output = new FileWriter(output);

            StringBuffer sb_output = new StringBuffer();
            String line;
            while((line= br_input_file.readLine()) != null){
                if( isContainChinese(line) && line.trim().length() > 1){
                    sb_output.append(line);
                    sb_output.append("@@n");
                    sb_output.append("\r\n");
                }
            }
            br_input_file.close();
            fw_output.write(sb_output.toString());
            fw_output.close();

        }

        public static boolean isContainDigital(String input) {
            int count = 0;
            String regEx = "^[十九八七六五四三二一百千万\\d]+[\\u4e00-\\u9fa5]*";

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(input);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count += 1;
                }
            }
            if (count == 0){
                return false;
            }
            return  true;
        }

        public static boolean isContainChinese(String input) {
            int count = 0;
            String regEx = "[\\u4e00-\\u9fa5]";

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(input);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count += 1;
                }
            }
            if (count == 0){
                return false;
            }
            return  true;
        }

        public static boolean isAllChinese(String input) {
            int count = 0;
            String regEx = "^[\\u4e00-\\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(input);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count += 1;
                }
            }
            if (count == 0){
                return false;
            }
            return  true;
        }

        public static boolean isContainNonChinese(String input) {
            int count = 0;
            String regEx = "[^\\u4e00-\\u9fa5]";

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(input);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count += 1;
                }
            }
            if (count == 0){
                return false;
            }
            return  true;
        }


        /**
         * 读取HDFS上的 word2v 模型文件
         * in_file 模型文件路径
         * dim_num 词向量维度
         * hm_feature_dict 缓存的哈希表
         */

        public static void loadStopWords(String in_file,Set<String> set_stop_words) throws IOException {
            BufferedReader br_in = readFile(in_file);
            String line;
            //String sep = " ";
            while ((line = br_in.readLine())!= null){
                String stop_word = line.trim();
                if (stop_word.equals(""))
                    continue;
                set_stop_words.add(stop_word);
            }

            br_in.close();
            return;
        }



        /**
         * 读取HDFS上的 word2v 模型文件
         * in_file 模型文件路径
         * dim_num 词向量维度
         * hm_feature_dict 缓存的哈希表
         */

        public static void loadW2vModelFile(String in_file,int dim_num,Map<String,float[]> hm_feature_dict) throws IOException {
            BufferedReader br_in = readFile(in_file);
            String line;
            String sep = " ";
            while ((line = br_in.readLine())!= null){
                String fields[] = line.split(sep);
                if (fields.length < dim_num + 1 )
                    continue;
                String word = fields[0];
                float feature_dim[] = new float[dim_num];
                for (int i = 1 ; i < fields.length; i++){
                    feature_dim[i-1] = Float.parseFloat(fields[i]);
                }
                hm_feature_dict.put(word, feature_dim);
            }

            br_in.close();
            return;
        }




        /**
         * 格式化float数组为字符串
         * @param sentence_vec
         * @return
         */
        public static String formatFloatArr(float[] sentence_vec,String seq){
            if (sentence_vec == null){
                System.err.print("arg is null in formatFloatArr");
                return "0";
            }

            StringBuffer sb_out = new StringBuffer();
            for (int i = 0; i < sentence_vec.length ; i++){
                sb_out.append(sentence_vec[i]);
                if (i < sentence_vec.length){
                    sb_out.append(seq);
                }
            }

            return sb_out.toString();
        }

        /**
         * 格式化float数组为字符串
         * @param sentence_vec
         * @return
         */
        public static String formatFloatArrWithIndex(float[] sentence_vec,String seq){
            if (sentence_vec == null){
                System.err.print("arg is null in formatFloatArr");
                return "0";
            }

            StringBuffer sb_out = new StringBuffer();
            for (int i = 0; i < sentence_vec.length ; i++){
                sb_out.append(i+1);
                sb_out.append(":");
                sb_out.append(sentence_vec[i]);
                if (i < sentence_vec.length){
                    sb_out.append(seq);
                }
            }

            return sb_out.toString();
        }
        //两个数值型数组相加
        public static float[] addFloatArr(float dest_A[],float src_B[]){
            if (dest_A.length != src_B.length || dest_A == null)
                return null;
            for (int i = 0 ; i < dest_A.length; i++){
                dest_A[i] += src_B[i];
            }
            return dest_A;
        }
        //数组均值：数组中的所有元素同除以同一个数
        public static float[] avgFloatArr(float dest_A[],int word_num){
            if (dest_A == null)
                return null;
            for (int i = 0 ; i < dest_A.length; i++){
                dest_A[i] /= word_num;
            }
            return dest_A;
        }




        /**
         * 将字符串中的过滤字符替换为，
         * @param content
         * @param hs_filter
         * @return
         */
        public static String filterCharacter(String content,HashSet<String> hs_filter){
            if (content.length()<1 )
                return null;
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<content.length(); i++){
                if (hs_filter.contains(content.charAt(i))){
                    sb.append(",");
                }else {
                    sb.append(content.charAt(i));
                }
            }
            return sb.toString();
        }

        public static String filterURL(String src){
            //String reg = "http://.";
            return src.replaceAll("www[0-9a-zA-Z.?&=/%]*|http[s:]?//[0-9a-zA-Z.]*/[0-9a-zA-Z.?&=/%]*|[0-9-— .]{1,14}|\\[[^\\]^\\[]{1,10}\\]|[^\\u4e00-\\u9fa5^a-zA-Z]"," ");
        }

        public static void proc (String input_file_path, String output_file_path, String filter_file_path) throws IOException {
            BufferedReader br_input_file;
            BufferedReader br_filter_file;
            Map<String,Integer> map_filter = new HashMap<String, Integer>();

            br_filter_file = new BufferedReader(new InputStreamReader(new FileInputStream(filter_file_path)));
            String line;
            while((line= br_filter_file.readLine()) != null){
                if(!map_filter.containsKey(line)){
                    map_filter.put(line, 1);
                }
            }
            br_filter_file.close();

            br_input_file = new BufferedReader(new InputStreamReader(new FileInputStream(input_file_path)));
            FileWriter fw_output = new FileWriter(output_file_path);
            String app_name;
            String app_desc_seg;
            StringBuffer sb_output = new StringBuffer();
            while((line= br_input_file.readLine()) != null){
                String fields[] = line.split("\t");
                if (fields.length < 2){
                    System.out.println(line);
                    continue;
                }
                app_name = fields[0];
                sb_output.append(app_name);
                sb_output.append('\t');
                app_desc_seg = fields[1];
                String arr_app_desc_seg[] = app_desc_seg.split("[, ]");
                for (String word : arr_app_desc_seg){
                    if(!map_filter.containsKey(word)  && isContainChinese(word) && !isContainDigital(word)){
                        sb_output.append(word);
                        sb_output.append(',');
                    }
                }

                sb_output.replace(sb_output.length() - 1, sb_output.length(), "\r\n");
            }
            br_input_file.close();
            fw_output.write(sb_output.toString());
            fw_output.close();

        }

        public static void test(){



        }

        //public  static void mapping(String source_file, String dim_file,)


        public  static void main(String[] args) throws IOException {
//            getKwd("兼职很简单只要10元 我拉你进群做任务 任务一般都是点赞投票之类的 做完后就给你红包 不同的任务佣金不同，几毛到十几不等，能干的一会就几十，懒的少，挣多少看自己。 要做的私我");
//        String input_path = args[0];
//        String output_path = args[1];
//        String filter_path = args[2];
            //proc(input_path,output_path,filter_path);
            //filter("F:/project/weibo/project/app_target/user_dict.txt","F:/project/weibo/project/app_target/user_dict_filter.txt");

            test();


        }

}
