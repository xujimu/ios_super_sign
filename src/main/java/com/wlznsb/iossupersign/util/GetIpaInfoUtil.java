package com.wlznsb.iossupersign.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.dd.plist.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class GetIpaInfoUtil {

    public static void main(String[] args) throws Exception {
        String a = "C:\\Users\\Administrator\\Desktop\\test1.ipa";
        File file = new File(a);
        String c = "C:\\Users\\Administrator\\Desktop\\123123.png";
        File file1 = new File(c);

        MyUtil.getIpaImg(c,c);

    }


    public static boolean verifyImage(InputStream inputStream) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(inputStream)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {

                return false;
            }
            ImageReader reader = iter.next();
            reader.setInput(iis, true);
            int width = reader.getWidth(0);
            //读取完整文件，速度太慢
            //BufferedImage image = ImageIO.read(inputStream);
            //int width = image.getWidth();
            return true;
        } catch (Exception e) {

            return false;
        }
    }


    public static Map<String, Object> readIPA(String ipaPath,String iconPath) {

        Map<String, Object> map = new HashMap<String, Object>();

        try {
            ArchiveInputStream zipIns = new ZipArchiveInputStream(new
                    FileInputStream(ipaPath), "UTF-8", false, true);

            ArchiveInputStream zipIns1 = new ZipArchiveInputStream(new
                    FileInputStream(ipaPath), "UTF-8", false, true);

            String size = zipIns.getBytesRead()  / 1024 / 1024 +"M";
            ArchiveEntry ze;
            ArchiveEntry ze1;
            while ((ze = zipIns.getNextEntry()) != null) {
                //如果不是文件夹
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    int length = name.split("/").length;
                    if (null != name && name.contains("Info.plist") &&  length == 3 ) {
                        String plistName = FileNameUtil.getName(name);
                        if(plistName.equals("Info.plist")){
                            NSDictionary  rootDict = (NSDictionary) PropertyListParser.parse(zipIns.readAllBytes());
                            NSObject cfBundleDisplayName = rootDict.objectForKey("CFBundleDisplayName");//应用名称
                            NSObject cfBundleIdentifier = rootDict.objectForKey("CFBundleIdentifier");//包名
                            NSObject cfBundleShortVersionString = rootDict.objectForKey("CFBundleShortVersionString");//版本号 8.0.20
                            NSObject cfBundleExecutable = rootDict.objectForKey("CFBundleExecutable");//应用解压路径名

                            map.put("package", cfBundleIdentifier);
                            map.put("versionName", cfBundleShortVersionString);
                            map.put("versionCode", cfBundleShortVersionString);
                            map.put("name", cfBundleDisplayName);
                            map.put("displayName", cfBundleDisplayName);
                            map.put("cfBundleExecutable", cfBundleExecutable);


                            //ipa大小
                            map.put("size", size);

                            String icon; //图标名字
                            NSObject iconName = rootDict.get("CFBundleIconFiles");

                            if(null != rootDict.get("CFBundleIconFiles")){
                                NSArray iconNameArr = (NSArray) iconName;
                                icon = iconNameArr.getArray()[iconNameArr.getArray().length - 1].toString();
                            }else {
                                NSDictionary iconName1 = (NSDictionary) rootDict.get("CFBundleIcons");
                                iconName1 =  (NSDictionary) iconName1.get("CFBundlePrimaryIcon");
                                NSArray iconNameArr = (NSArray) iconName1.get("CFBundleIconFiles");
                                icon = iconNameArr.getArray()[iconNameArr.getArray().length - 1].toString();
                            }

                            while ((ze1 = zipIns1.getNextEntry()) != null){
                                if (!ze1.isDirectory()) {
                                    if(ze1.getName().contains(icon)){
                                        FileUtil.writeBytes(zipIns1.readAllBytes(),new File(iconPath));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            zipIns1.close();
            zipIns.close();

        }catch (Exception e){
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
