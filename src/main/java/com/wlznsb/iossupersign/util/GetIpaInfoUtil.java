package com.wlznsb.iossupersign.util;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class GetIpaInfoUtil {

    public static void main(String[] args) {
        String ipaUrl = "C:\\Users\\xujimu\\Desktop\\123.ipa";
        String imgPath = "C:\\Users\\xujimu\\Desktop\\aaa.png";
        Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaUrl,imgPath);
        System.out.println(mapIpa.get("package"));
        for (String key : mapIpa.keySet()) {
            System.out.println(key + ":" + mapIpa.get(key));
        }
    }
    /**
     *
     * @param ipaURL 安装包的绝对路径
     * @param path 指定图标的存放位置
     * @return
     */
    public static Map<String, Object> readIPA(String ipaURL,String path) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            File file = new File(ipaURL);
            InputStream is = new FileInputStream(file);
            String size = is.available() / 1024 / 1024 +"M";
            InputStream is2 = new FileInputStream(file);
            ZipInputStream zipIns = new ZipInputStream(is);
            ZipInputStream zipIns2 = new ZipInputStream(is2);
            ZipEntry ze;
            ZipEntry ze2;
            InputStream infoIs = null;
            NSDictionary rootDict = null;
            String icon = null;
            while ((ze = zipIns.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    if (null != name && name.toLowerCase().contains("info.plist")) {
                        if(name.length() -2 != name.replace("/", "").length()){
                            continue;
                        }
                        ByteArrayOutputStream _copy = new ByteArrayOutputStream();
                        int chunk = 0;
                        byte[] data = new byte[1024];
                        while (-1 != (chunk = zipIns.read(data))) {
                            _copy.write(data, 0, chunk);
                        }
                        infoIs = new ByteArrayInputStream(_copy.toByteArray());

                        rootDict = (NSDictionary) PropertyListParser.parse(infoIs);

                        NSDictionary iconDict = (NSDictionary) rootDict.get("CFBundleIcons");

                        //获取图标名称
                        while (null != iconDict) {
                            if (iconDict.containsKey("CFBundlePrimaryIcon")) {
                                NSDictionary CFBundlePrimaryIcon = (NSDictionary) iconDict.get("CFBundlePrimaryIcon");
                                if (CFBundlePrimaryIcon.containsKey("CFBundleIconFiles")) {
                                    NSArray CFBundleIconFiles = (NSArray) CFBundlePrimaryIcon.get("CFBundleIconFiles");
                                    //读取最大的图片
                                    icon = CFBundleIconFiles.getArray()[CFBundleIconFiles.getArray().length - 1].toString();
                                    if (icon.contains(".png")) {
                                        icon = icon.replace(".png", "");
                                    }
                                    System.out.println("获取icon名称:" + icon);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            //根据图标名称下载图标文件到指定位置
            while ((ze2 = zipIns2.getNextEntry()) != null) {
                if (!ze2.isDirectory()) {
                    String name = ze2.getName();
                    if (icon!=null){
                        if (name.contains(icon.trim())) {
                            //图片下载到指定的地方
                            FileOutputStream fos = new FileOutputStream(new File(path));
                            int chunk = 0;
                            byte[] data = new byte[1024];
                            while (-1 != (chunk = zipIns2.read(data))) {
                                fos.write(data, 0, chunk);
                            }
                            fos.close();
                            System.out.println("=================下载图片成功");
                            break;
                        }
                    }
                }
            }


            //如果想要查看有哪些key ，可以把下面注释放开
//       for (String string : dictionary.allKeys()) {
//          System.out.println(string + ":" + dictionary.get(string).toString());
//       }


            // 应用包名
            NSString parameters = (NSString) rootDict.get("CFBundleIdentifier");

            map.put("package", parameters.toString());
            // 应用版本名
            parameters = (NSString) rootDict.objectForKey("CFBundleShortVersionString");
            map.put("versionName", parameters.toString());
            //应用版本号
            parameters = (NSString) rootDict.get("CFBundleVersion");
            map.put("versionCode", parameters.toString());
            //应用名称
            parameters = (NSString) rootDict.objectForKey("CFBundleName");
            map.put("name", parameters.toString());
            //应用展示的名称
            parameters = (NSString) rootDict.objectForKey("CFBundleDisplayName");
            if(parameters != null){
                map.put("displayName", parameters.toString());
            }else {
                map.put("displayName", map.get("name"));
            }
            //ipa大小
            map.put("size", size);
            //应用所需IOS最低版本
            //parameters = (NSString) rootDict.objectForKey("MinimumOSVersion");
            //map.put("minIOSVersion", parameters.toString());

            infoIs.close();
            is.close();
            zipIns.close();

        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", "fail");
            map.put("error", "读取ipa文件失败");
        }
        return map;
    }

    /**
     * 创建plist文件
     * @param path
     * @return
     * @throws IOException
     */
    public static String createPlist(String path) throws IOException{
        System.out.println("==========开始创建plist文件");
        //这个地址应该是创建的服务器地址，在这里用生成到本地磁盘地址
        File file = new File(path);
        String plist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
                + "<plist version=\"1.0\">\n" + "<dict>\n"
                + "<key>items</key>\n"
                + "<array>\n"
                + "<dict>\n"
                + "<key>assets</key>\n"
                + "<array>\n"
                + "<dict>\n"
                + "<key>kind</key>\n"
                + "<string>software-package</string>\n"
                + "<key>url</key>\n"
                //你之前所上传的ipa文件路径
                + "<string>http://127.0.0.1/project/upload/files/20160504201048174_7836_19.ipa</string>\n"
                + "</dict>\n"
                + "</array>\n"
                + "<key>metadata</key>\n"
                + "<dict>\n"
                + "<key>bundle-identifier</key>\n"
                //这个是开发者账号用户名，也可以为空，为空安装时看不到图标，完成之后可以看到
                + "<string>cn.vrv.im-inhouse</string>\n"
                + "<key>bundle-version</key>\n"
                + "<string>1.0.7</string>\n"
                + "<key>kind</key>\n"
                + "<string>software</string>\n"
                + "<key>subtitle</key>\n"
                + "<string>下载</string>\n"
                + "<key>title</key>\n"
                + "<string></string>\n"
                + "</dict>\n"
                + "</dict>\n"
                + "</array>\n"
                + "</dict>\n"
                + "</plist>";
        try {
            FileOutputStream output = new FileOutputStream(file);
            OutputStreamWriter writer;
            writer = new OutputStreamWriter(output, "UTF-8");
            writer.write(plist);
            writer.close();
            output.close();
        } catch (Exception e) {
            System.err.println("==========创建plist文件异常：" + e.getMessage());
        }
        System.out.println("==========成功创建plist文件");
        return path;
    }

}
