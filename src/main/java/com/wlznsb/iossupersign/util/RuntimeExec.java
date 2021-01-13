package com.wlznsb.iossupersign.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RuntimeExec {

    /**
     * 传入执行的命令
     * 返回map,有两个key,一个是status一个是info
     * status返回执行状态码 0.执行成功 1.命令执行失败 -1.系统无法执行命令或命令不存在
     * info返回结果信息
     * 没有采用多线程,会导致线程阻塞
     * @param cmd
     * @return
     */
    public static  Map<String,Object> runtimeExec(String cmd) {
        Map<String,Object> map = new HashMap<String, Object>();

        try {
            Process p;
            //执行命令
            p = Runtime.getRuntime().exec(cmd);
            InputStream fis;
            //判断运行结果
            if(p.waitFor() == 0){
                //取得命令结果的输出流
                 fis=p.getInputStream();
                map.put("status", 0);
            }else {
                //取得命令结果的输出流
                 fis=p.getErrorStream();
                map.put("status", 1);
            }
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line = "";
            String result = "";
            //直到读完为止
            while((line=br.readLine())!=null)
            {
                result = result + line;
            }
            map.put("info", result);

            fis.close();
        }  catch (Exception e)
        {
            map.put("status", -1);
            map.put("info", e.toString());
        }finally {
            return map;
        }

    }


}
