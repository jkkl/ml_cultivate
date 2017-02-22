package util;

import java.util.Arrays;

/**
 * Created by yuanzhuo on 2017/2/21.
 */
public class realloc {

    public static <T> T[] realloc(T[] src,int newLen){
        newLen = newLen > 0 ? newLen : 0;
        return Arrays.copyOf(src,newLen);
    }
 }
