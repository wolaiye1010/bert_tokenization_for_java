package com.bj58.hy.strategy.bert.tokenizer;

import java.io.*;

/**
 * Created by zhudongchang on 2017/4/24.
 */
public class FileUtils {
    /**
     * 创建文件
     * 目录不存在时递归 创建目录
     * 创建成功 返回true  否则返回false
     * @param pathname
     * @return
     */
    public static boolean createFile(String pathname){
        File file=new File(pathname);
        if(file.exists()){
            return false;
        }

        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            if(!parentFile.mkdirs()){
                return false;
            }
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件内容
     * @param pathname
     * @return
     */
    public static String fileGetContent(String pathname){
        File file=new File(pathname);

        FileInputStream fileInputStream=null;
        try {
            StringBuilder stringBuilder=new StringBuilder();
            fileInputStream=new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream,"utf-8");
            char[]readBuffer=new char[1000];

            int readLength;

            while (-1!=(readLength=reader.read(readBuffer))){
                stringBuilder.append(new String(readBuffer,0,readLength));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 写入文件内容
     * @param pathname
     * @param data
     */
    public static void filePutContent(String pathname, String data){
        filePutContent(pathname,data,false);
    }

    /**
     * 写入文件内容 文件不存在时创建（递归创建）
     * @param pathname
     * @param data
     * @param append
     */
    public static void filePutContent(String pathname, String data, boolean append){
        FileOutputStream fileOutputStream=null;
        try {
            File file=new File(pathname);
            if(!file.exists()){
                if(!createFile(pathname)){
                    throw new RuntimeException(String.format("%s 文件创建失败,请检查是否有相应权限",pathname));
                }
            }

            if(null==data){
                data="";
            }

            fileOutputStream=new FileOutputStream(file,append);
            fileOutputStream.write(data.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(null!=fileOutputStream){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 删除文件 成功返回true 失败返回false
     * @param pathname
     * @return
     */
    public static boolean deleteFile(String pathname){
        File file=new File(pathname);
        return file.delete();
    }
}
