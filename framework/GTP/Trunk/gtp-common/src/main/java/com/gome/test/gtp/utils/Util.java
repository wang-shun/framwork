/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.utils;


import com.gome.test.utils.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author shaoxuan.feng
 */
public class Util {

    public static String fromClob(Clob clob) {
        String reString = "";
        Reader is = null;
        try {
            is = clob.getCharacterStream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 得到流
        BufferedReader br = new BufferedReader(is);
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            try {
                s = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reString = sb.toString();
        return reString;

    }

    public static String timestamp2String(Timestamp timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        String str = df.format(timestamp);
        return str;
    }

    public static String long2String(long time) {
        Timestamp timestamp = new Timestamp(time);
        return timestamp2String(timestamp);
    }

    public static String null2String(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    public static String dateToString(java.util.Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);//定义格式，不显示毫秒
        return df.format(date);
    }

    public static String dateToString(java.util.Date date) {
        return dateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Timestamp string2Timestamp(String str) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);

        Timestamp ts = new Timestamp(df.parse(str).getTime());
//        System.out.println("timestamp:" + ts.toString());
        return ts;
    }

    public static Timestamp StringToTimestamp(String str) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setLenient(false);

        Timestamp ts = new Timestamp(df.parse(str).getTime());
        return ts;
    }

    public static String dateBeforeToday(int beforeDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -beforeDays);
        java.util.Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date);
    }

    public static int spaceNumOfString(String str1, String str2) {
        int counter = 0;
        for (int i = 0; i <= str1.length() - str2.length(); i++) {
            if (str1.substring(i, i + str2.length()).equalsIgnoreCase(str2)) {
                counter++;
            }
        }
        return counter;
    }

    public static String appendTaskLog(String log, String... append) {
        if (log == null)
            log = "";
        StringBuffer strB = new StringBuffer(log);
        for (String s : append) {
            strB.append(String.format("[%s] ", dateToString(new java.util.Date(System.currentTimeMillis()))));
            strB.append(s);
            strB.append("\n");
        }
        return strB.toString();
    }

    public static Date ymdNowDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        f.format(date);
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public static Timestamp nowTimestamp () {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(df.format(new java.util.Date()));
    }

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    public static boolean matchPath(String setUrl, String url) {
        if(setUrl.equals(url)) {
            return true;
        }
        String[] setUrlArray = setUrl.split("/");
        String[] urlArray = url.split("/");
        if(setUrlArray[setUrlArray.length-1].equals("*") && setUrlArray.length == urlArray.length) {
            for(int i = 0; i < setUrlArray.length-1; i++) {
                if(setUrlArray[i].equals(urlArray[i]) == false){
                    return false;
                }
            }
        } else {
            return false;
        }
//        System.out.println(setUrl);
        Logger.info(setUrl);
        return true;
    }


    //读取文件
    public static BufferedReader getReader(File file) throws Exception {

        int bufferSize = 20 * 1024 * 1024; //设读取文件的缓存为20MB
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream,"UTF-8");
        BufferedReader input = new BufferedReader(inputStreamReader, bufferSize);
        return input;
    }


    public static void writeLine(String lineMess,String filePath) throws Exception
    {
        OutputStreamWriter output = null;
        try {
            output = new OutputStreamWriter(new FileOutputStream(filePath, true),"UTF-8");

            output.write(lineMess + "\r\n");

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (output != null)
                output.close();
        }
    }

    public static double setScaleFour(double f,int Scale)
    {
        if (f > Double.MAX_VALUE)
            f = Double.MAX_VALUE;
        BigDecimal b   =   new   BigDecimal(f);
        double result= b.setScale(Scale,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return result ;
    }

    public static String join(List<String> stringList) {
        StringBuilder sb = new StringBuilder();
        for (String str : stringList) {
            sb.append(str);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }


    public static String replacePropsFromIs(Map<String, String> repMap, InputStream fis) throws IOException {
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String line;
        int index;
        String key;
        String repStr;
        String resultCfgStr = "";
        int lineNum = 1;
        while ((line = br.readLine()) != null) {
            while (line.contains("=")) {
                index = line.indexOf("=");
                key = line.substring(0, index).trim();
                repStr = line.substring(index + 1, line.length());
                if (repMap.keySet().contains(key)) {
                    line = line.replaceAll(repStr, repMap.get(key));
                    break;
                } else {
                    break;
                }
            }
//            System.out.print(lineNum + " " + line + "\r\n");
            Logger.info(lineNum + " " + line + "\r\n");
            lineNum++;
            resultCfgStr = resultCfgStr + line + "\r\n";
        }
        br.close();
        isr.close();
        fis.close();
        return resultCfgStr;
    }

    public static String computePercent(int fenzi, int fenmu, int xiaoshuwei) {
        if (fenmu != 0) {
            double passRate = (double) fenzi / (double) fenmu;
            String xiaoshu = "";
            while (xiaoshuwei > 0) {
                xiaoshu = xiaoshu + "0";
                xiaoshuwei --;
            }
            NumberFormat nf = new DecimalFormat("0." + xiaoshu);
            return nf.format(passRate*100) + "%";
        } else {
            return "非法结果";
        }
    }

    public static void main(String[] args) throws IOException {
//        List<String> stringList = new ArrayList<String>();
//        stringList.add("1");
//        stringList.add("2");
//        System.out.println(join(stringList));

//        File file = new File("D:\\svn\\SVNCode\\Doraemon\\GTP\\Trunk\\gtp-domain\\src\\main\\resources\\application.properties");
//        InputStream is = new FileInputStream(file);
//        Map<String, String> repMap = new HashMap<String, String>();
//        repMap.put("jenkins.ip","10.144.35.116");
//        replacePropsFromIs(repMap, is);


//        int fenzi = 87;
//        int fenmu = 88;
//        int xiaoshuwei = 2;
//            double passRate = (double) fenzi / (double) fenmu;
//            String xiaoshu = "";
//            while (xiaoshuwei > 0) {
//                xiaoshu = xiaoshu + "0";
//                xiaoshuwei--;
//            }
//            NumberFormat nf = new DecimalFormat("0." + xiaoshu);
//            System.out.println(nf.format(passRate*100) + "%");
////        System.out.println(computePercent(232, 243, 2));

        System.out.println(Util.dateBeforeToday(-3));

    }
}
