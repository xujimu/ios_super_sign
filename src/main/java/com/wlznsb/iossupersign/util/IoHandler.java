package com.wlznsb.iossupersign.util;

import java.io.*;
import java.util.Base64;

public class IoHandler {
    /**传入txt路径读取txt文件
     * @param txtPath
     * @return 返回读取到的内容
     */
    public static String readTxt(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer sb = new StringBuffer();
                int text;
                while((text = bufferedReader.read()) != -1){
                    sb.append((char) text);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**使用FileOutputStream来写入txt文件
     * @param txtPath txt文件路径
     * @param content 需要写入的文本
     */
    public static void writeTxt(String txtPath,String content){
        FileOutputStream fileOutputStream = null;
        File file = new File(txtPath);
        try {
            if(file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通的FileOutputStream写入文件没有编码问题
     *
     */
    public static void fileWriteTxt(String txtPath,byte[] content) {
        try {
            OutputStream out = new FileOutputStream(txtPath);
            out.write(content);
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 删除文件
     */

    public static void  deleFile(String filePath){
        File file = new File(filePath);
        file.delete();
    }

    /**
     * 获取spring boot 资源路径的决定路径
     */



}

