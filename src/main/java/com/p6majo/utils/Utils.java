package com.p6majo.utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void print(String string){
        System.out.println(string);
    }

    public static void errorMsg(String text){
        System.err.println(text);
        System.exit(0);
    }




    public static double map(double value, double srcMin, double srcMax, double imgMin, double imgMax){ return value*(imgMax-imgMin)/(srcMax-srcMin); }

    public static int map(double value, double srcMin, double srcMax, int imgMin, int imgMax){return (int) Math.round(value*(imgMax-imgMin)/(srcMax-srcMin)); }


    /**
     * Display an array as string
     * Optionally the string segment for each object can be given
     * @param array
     * @param <T>
     * @return
     */
    public static <T extends Object> String array2String(T[] array, Integer l){
        String out="[";
        for (int i=0;i<array.length;i++)
            if (array instanceof Double[])
                if (l!=null){
                    double a = Math.round((Double) array[i]*Math.pow(10,l))/Math.pow(10,l);
                    String s = String.format("%.19f",a);
                    s=s.substring(0,l+1);
                    out+=s+";";
                }
                else out+=String.format("%.10f",array[i])+";";
            else
                out+=array[i].toString()+",";
        if (l!=null) out=out.substring(0,out.length()-2);
        else out = out.substring(0,out.length()-2);
        out+="]";
        return out;
    }

    public static<T extends Object> String array2String(T[] array){
        return array2String(array,null);
    }

    /**
     * generate URL for a file on the local storage such that it can be accessed via browsers
     * @param filename
     * @return
     */
    public static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }

    /**
     * get Time string that can be used to have a unique file name
     * @return
     */
    public static String getGMTTimeString(){
        try{
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

            //Local time zone
            SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

            //Time in GMT
            return dateFormatLocal.parse( dateFormatGmt.format(new Date()) ).toString();
        }
        catch(ParseException ex){
            Utils.errorMsg(ex.getMessage());
        }
        return null;
    }

    public static String intArray2String(int[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket, 0, 1);
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(array[i]);
            }
            if (bracket.length()>1) output.append(bracket, 1, 2);
        }
        return output.toString();
    }

    public static String int2dArray2String(int[][] array,int width, int height, String sep, String bracket){
        StringBuilder output = new StringBuilder();
        if (bracket.length() > 1) output.append(bracket, 0, 1);
        for (int w = 0; w < width; w++) {
            String prefix = "";
            if (bracket.length() > 1) output.append(bracket, 0, 1);
            for (int h = 0; h < height; h++) {
                    output.append(prefix);
                    prefix = sep;
                    output.append(array[h][w]);
                }
            if (bracket.length() > 1) output.append(bracket, 1, 2);
            output.append("\n");
        }
        if (bracket.length() > 1) output.append(bracket, 1, 2);
        return output.toString();
    }

    public static int[] intArrayFromString(String data, String sep, String bracket){
       for (int s = 0;s<bracket.length();s++)
           data.replace(""+bracket.charAt(s),"");

       StringTokenizer tokens = new StringTokenizer(data,sep);
       int[] out = new int[tokens.countTokens()];

       int count = 0;
       while (tokens.hasMoreTokens()) {
            out[count] = Integer.valueOf(tokens.nextToken());
            count++;
       }

       return out;
    }

    public static String longArray2String(long[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket, 0, 1);
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(array[i]);
            }
            if (bracket.length()>1) output.append(bracket, 1, 2);
        }
        return output.toString();
    }

    public static String floatArray2String(float[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket, 0, 1);
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(String.format("%.4f",array[i]));
            }
            if (bracket.length()>1) output.append(bracket, 1, 2);
        }
        return output.toString();
    }

    public static String doubleArray2String(double[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket, 0, 1);
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(array[i]);
            }
            if (bracket.length()>1) output.append(bracket, 1, 2);
        }
        return output.toString();
    }

    public static String booleanArray2String(Boolean[] array,String sep, String bracket){
        StringBuilder output = new StringBuilder();
        String prefix = "";
        if (array.length>0){
            if (bracket.length()>1) output.append(bracket, 0, 1);
            for (int i=0;i<array.length;i++) {
                output.append(prefix);
                prefix = sep;
                output.append(array[i]);
            }
            if (bracket.length()>1) output.append(bracket, 1, 2);
        }
        return output.toString();
    }

    public static void floodFill(int[][] array,int width,int height,int[] pos,int floodValue, int fillValue){

        Stack<int[]> stack = new Stack<int[]>();
        stack.push(pos);

        while (!stack.empty()){
            int[] tmpPos = stack.pop();

            if (array[tmpPos[1]][tmpPos[0]]==floodValue) {
                array[tmpPos[1]][tmpPos[0]]=fillValue;
                if (tmpPos[0]>0) stack.push(new int[]{tmpPos[0]-1,tmpPos[1]});
                if (tmpPos[0]<width-1) stack.push(new int[]{tmpPos[0]+1,tmpPos[1]});
                if (tmpPos[1]>0) stack.push(new int[]{tmpPos[0],tmpPos[1]-1});
                if (tmpPos[1]<height-1) stack.push(new int[]{tmpPos[0],tmpPos[1]+1});
            }
        }
    }

    public static float[] floatMatrix2Vector(float[][] array,int width, int height){
        float[] vector = new float[width*height];
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                vector[h+w*height]=array[w][h];
            }
        }
        return vector;
    }

    public static float[] floatMatrix2Vector(float[][] array,int width, int height,float bias,float scale){
        float[] vector = new float[width*height];
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                vector[h+w*height]=(array[w][h]+bias)*scale;
            }
        }
        return vector;
    }

    public static BufferedImage floatMatrix2BufferedImage(float[][] array, int width, int height,float min,float max){

        BufferedImage image =new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

        WritableRaster raster = image.getRaster();

        float delta = max-min;
        float scale = 256/delta;

        float[] vector = Utils.floatMatrix2Vector(array,width,height,-min,scale);


        raster.setPixels(0,0,width,height,vector);
        return image;
    }

    public static BufferedImage floatVector2BufferedImage(float[] vector, int width, int height,float min,float max){

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();

        float delta = max-min;
        float scale = 256/delta;

        //rescale values into the interval [0,256)
        for (int i = 0; i < vector.length; i++)
            vector[i]=(vector[i]-min)*scale;

        raster.setPixels(0,0,width,height,vector);
        return image;
    }

    /**
     * convert all numbers into indices
     * @param input
     * @return
     */
    public static String getIndices(String input){
        String str = input;
        str = str.replaceAll("0", "₀");
        str = str.replaceAll("1", "₁");
        str = str.replaceAll("2", "₂");
        str = str.replaceAll("3", "₃");
        str = str.replaceAll("4", "₄");
        str = str.replaceAll("5", "₅");
        str = str.replaceAll("6", "₆");
        str = str.replaceAll("7", "₇");
        str = str.replaceAll("8", "₈");
        str = str.replaceAll("9", "₉");
        str = str.replaceAll("_", "");
        return str;
    }

    /**
     * convert all numbers into super scripts
     * @param input
     * @return
     */
    public static String getSuperScript(String input){
        String str = input;

        str = str.replaceAll("0", "⁰");
        str = str.replaceAll("1", "¹");
        str = str.replaceAll("2", "²");
        str = str.replaceAll("3", "³");
        str = str.replaceAll("4", "⁴");
        str = str.replaceAll("5", "⁵");
        str = str.replaceAll("6", "⁶");
        str = str.replaceAll("7", "⁷");
        str = str.replaceAll("8", "⁸");
        str = str.replaceAll("9", "⁹");
        str = str.replaceAll("^", "");

        return str;
    }


    /****************************************/
    /*** for groups *************************/
    /****************************************/

    public static String makeCSListFromSet(Set<?> set){
        String output = "";
        if (set.size()>0){
            output+="{";
            Iterator<?> it = set.iterator();
            while(it.hasNext()) output+=it.next().toString()+",";
            output=output.substring(0,output.length()-1)+"}";
        }
        return output;
    }

    public static String makeCSListFromSet(List<Integer> list){
        String output = "";
        if (list.size()>0){
            output+="{";
            for (Integer element:list) output+=element.toString()+",";
            output=output.substring(0,output.length()-1)+"}";
        }
        return output;
    }

    public static String makeCSListFromSet(Set<?> set,String sep, String bracket){
        String output = "";
        if (set.size()>0){
            output+=bracket.substring(0,1);
            Iterator<?> it = set.iterator();
            while(it.hasNext()) output+=it.next().toString()+sep;
            output=output.substring(0,output.length()-1)+bracket.substring(1,2);
        }
        return output;
    }



    //has to be written separately since I cannot add the interface Html to the final class Integer
    public static String makeCSHtmlListFromSet(TreeSet<Integer> set,String sep, String bracket){
        String output = "";
        if (set.size()>0){
            output+=bracket.substring(0,1);
            Iterator<Integer> it = set.iterator();
            while(it.hasNext()) output+=it.next().toString()+sep;
            output=output.substring(0,output.length()-1)+bracket.substring(1,2);
        }
        return output;
    }

    public static String makeCSListFromArray(short[] array,String sep, String bracket){
        String output = "";
        if (array.length>0){
            if (bracket.length()>1) output+=bracket.substring(0,1);
            for (int i=0;i<array.length;i++) output+=array[i]+sep;
            output=output.substring(0,output.length()-1);
            if (bracket.length()>1) output+=bracket.substring(1,2);
        }
        return output;
    }


    /**
     * converts latex subscripts to html-subscripts
     * @param input
     * @return
     */
    public static String string2html(String input){
        String output = "";
        StringTokenizer factors = new StringTokenizer(input,"x");
        while (factors.hasMoreTokens()){
            StringTokenizer factor = new StringTokenizer(factors.nextToken(),"_");
            if (factor.countTokens()==1) output+=factor.nextToken()+"x";
            else {
                output+=factor.nextToken()+"<sub>"+factor.nextToken()+"</sub>"+"x";
            }
        }
        //remove last "x"
        output=output.substring(0,output.length()-1);
        return output;
    }

    public static int subPermutations(int n,int k){
        if (k==0) return 1;
        else return (n-k+1)*subPermutations(n,k-1);
    }


    private static int[] nextCombination(int[] previous, int n, int k,int start){
        int[] next = new int[k];
        for (int i=0;i<k;i++) next[i] = previous[i];

        boolean success = false;
        int lastDigit = k-1;
        while (!success){
            if (next[lastDigit]<n+start) {
                next[lastDigit]++;
                // only successful if the following digits can be filled with larger numbers
                if (next[lastDigit]<n+start-(k-1-lastDigit)){
                    for (int i=lastDigit+1;i<k;i++) next[i]
                            = next[i-1]+1;
                    success = true;
                }
            }
            else lastDigit--;
            if (lastDigit<0) return null;
        }

        return next;
    }


    /**
     * calculate the binomial coefficient (n k)
     * @param n
     * @param k
     * @return
     */
    public static long combinations(int n,int k){
        if (k<0 || k>n) return 0;
        if (k>n-k) k=n-k;
        long result = 1;
        for (int i=0;i<k;i++) {
            result*=(n-i);
            result/=(i+1);
        }
        return result;
    }

    /**
     * Returns the set of all permutations of k elements out of n elements
     * @param n
     * @param k
     * @return
     */
    public static int[][] intCombinations(int n, int k){
        return intCombinations(n,k,1);
    }

    /**
     * Returns the set of all permutations of k elements out of n elements
     * the n elements startWuerfeln with startWuerfeln and end with startWuerfeln+n
     * @param n
     * @param k
     * @return
     */
    public static int[][] intCombinations(int n, int k,int start){
        if (k>n) Utils.errorMsg("Wrong parameters in intCombinations: n="+n+" and k="+k+"!");

        //initialize the array of combinations
        long tmpcount =  Utils.combinations(n, k);
        if (tmpcount>2147483647) Utils.errorMsg("Size limit exceeded in intCombinations: "+tmpcount+"!");
        int size = (int) tmpcount;
        int[][] combinations = new int[size][k];

        //first combination
        for (int i=0;i<k;i++) combinations[0][i]=i+start;

        int[] next = combinations[0];
        int counter = 1;

        while (next!=null){
            next = nextCombination(next,n,k,start);
            if (next!=null) {
                for (int i=0;i<k;i++) combinations[counter][i]=next[i];
                counter++;
            }
        }

        return combinations;
    }

    /**
     * Returns the set of all permutations of k elements out of n elements
     * @param n
     * @param k
     * @return
     */
    public static Integer[][] IntCombinations(int n, int k){
        if (k>n) Utils.errorMsg("Wrong parameters in intCombinations: n="+n+" and k="+k+"!");

        //initialize the array of combinations
        long tmpcount =  Utils.combinations(n, k);
        if (tmpcount>2147483647) Utils.errorMsg("Size limit exceeded in intCombinations: "+tmpcount+"!");
        int size = (int) tmpcount;
        Integer[][] combinations = new Integer[size][k];

        //first combination
        int[] first = new int[k];
        for (int i=0;i<k;i++) first[i]=i+1;
        for (int i=0;i<k;i++) combinations[0][i]=first[i];

        int[] next = first;
        int counter = 1;

        while (next!=null){
            next = nextCombination(next,n,k,1);
            if (next!=null) {
                for (int i=0;i<k;i++) combinations[counter][i]=next[i];
                counter++;
            }
        }

        return combinations;
    }


    /**
     * Returns the set of all permutations of k elements out of n elements
     * @param n
     * @param k
     * @return
     */
    public static int[][] intPermutations(int n, int k){
        int tmpcount =  Utils.subPermutations(n, k);
        int[][] perms = new int[tmpcount][k];
        if (k==1) {
            for (int i=1;i<n+1;i++){
                perms[i-1][0]=i;
            }
            return perms;
        }
        else {
            int count=0;
            int[][] tmps = intPermutations(n,k-1);
            for (int i = 0; i< Utils.subPermutations(n, k-1); i++){
                for (int j=1;j<n+1;j++){
                    boolean contained = false;
                    for (byte l=1;l<k;l++){
                        if (j==tmps[i][l-1]) {contained = true;break;}
                    }
                    if (!contained){
                        for (int l=1;l<k;l++) perms[count][l-1]=tmps[i][l-1];
                        perms[count][k-1]=j;
                        count++;
                    }
                }
            }
            return perms;
        }
    }


    /**
     * returns the floor of the squareRootBinComparator
     * @param x2
     * @return
     */
    public static BigInteger sqrtFloor(BigInteger x2){
        if (x2.compareTo(BigInteger.ZERO)==0) return x2;
        BigInteger div = BigInteger.ZERO.setBit(x2.bitLength()/2);
        BigInteger div2 = div;
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for(;;) {
            BigInteger y = div.add(x2.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }

}
