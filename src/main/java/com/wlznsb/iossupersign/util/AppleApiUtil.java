package com.wlznsb.iossupersign.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import net.odyssi.asc4j.util.TokenUtil;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

/**
 *
 * 访问苹果api的工具类
 *
 */
@Slf4j
public class AppleApiUtil {


    private String token;
    private String iis;
    private String kid;
    private String p8;
    private RestTemplate restTemplate;
    private HttpEntity httpEntity;

    //申请证书的json
    private String certificates = "{\"data\":{\"type\":\"certificates\",\"attributes\":{\"certificateType\":\"IOS_DISTRIBUTION\",\"csrContent\":\"MIICfDCCAWQCAQAwNzE1MDMGCSqGSIb3DQEJARYmbXllbWFpbEBzYW1wbGUuY29tLENOPUNvbW1vbiBOYW1lLEM9Q04wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCZl2ZUQ+GzHs080tpCoy4eESNLHIfzRb4ShiMxYrOHVJepFJsS+Bwq3k7m8WlLFz1PBP0MDp0pyslNXQkgTwtl5ceitfCYZztVKFJXptN52TwHQiIo0L+MNMG3X6Wk7Gh6O1vRJ\\/k1Jn+bJ3dviBnoGpYifu1MZIumTfdgxNEtcKMe9OYt4ZJUsBgkJcgGWuF3uhahxAlI8r\\/Nb78QFXkEFix+ZkwaWvQbym7BGDRWwcU8yTo2Gpz072V+floSNDT5FpuqKM27yYSOItKO3Rw7NMw2OGaf6I1AacM2M1Ayh\\/eASXdmd7l5evWEDP5mLzqAkqGQHRE2gpYyx0XF0CNnAgMBAAGgADANBgkqhkiG9w0BAQsFAAOCAQEALSU+4+SinTnDH1\\/pAPb6sZYlKz5Azp4Mrmkay8W0\\/YycN4ZSVa+1Hg2LxHF4gMuOVbi1I0uz1EdqPGEgHqMjPsiV0Z3ih7FE42a6mhOId\\/MHh9RiGE8qb2l\\/bjaKLClDdJjoTlpyGSRaZTiDBJYSGD1UZFsbYlhhavCDiKaWGd8MRc4Gxo6FG8c3ffGea3jhvo8+FbHgcVCTCy3cg3im76zZi3uNWHsB6oxgHyWmoo\\/q1X9Q30MsBFhBv2nKOIfPqEPhozqj6QPRCNLLpJwbojZLfP89howC7n60u9UrDr88lO\\/9O38Qx1QOWQAkZBdsvVZ5uABdYzn7c+6xePmHZA==\"}}}";
    public AppleApiUtil(String iis, String kid, String p8) {
        this.iis = iis;
        this.kid = kid;
        this.p8 = p8;
        //初始化restTemplate
        this.restTemplate = new RestTemplate();
    }

    public AppleApiUtil() {

    }

    /**
     * 检验证书和信息是否正确
     * @return
     */
    public boolean init(){
        try {
            Long time = System.currentTimeMillis();
            this.token = TokenUtil.generateToken(this.iis, this.kid,this.p8);
            System.out.println(this.token);
            //设置请求头参数
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Bearer " + token);
            this.httpEntity = new HttpEntity(requestHeaders);
            //执行
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/apps",
                    HttpMethod.GET,httpEntity, String.class);
            log.info("初始化耗费时间:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return true;
        }catch (Exception e){
            log.info(e.toString());
            return false;
        }
    }



    /**
     * 检验证书和信息是否正确
     * @return
     */
    public boolean initTocken(){
        try {
            Long time = System.currentTimeMillis();
            this.token = TokenUtil.generateToken(this.iis, this.kid,this.p8);
            log.info(this.token);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 尝试删除所有证书
     * @throws JsonProcessingException
     */
    public void deleCertAll() {
        try {
            //查询所有证书
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/certificates",
                    HttpMethod.GET,this.httpEntity, String.class);
            //序列化返回
            JsonNode json = new ObjectMapper().readTree(exchange.getBody()).get("data");
            //删除所有证书
            for (JsonNode obj:json){
                restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/certificates/"+ obj.get("id").asText(),
                        HttpMethod.DELETE,this.httpEntity, String.class);
            }
            //查询所有配置文件
            exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/profiles",
                    HttpMethod.GET,this.httpEntity, String.class);
            //序列化返回
            json = new ObjectMapper().readTree(exchange.getBody()).get("data");

            //删除所有配置文件
            for (JsonNode obj:json){
                restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/profiles/"+ obj.get("id").asText(),
                        HttpMethod.DELETE,this.httpEntity, String.class);
            }

            //查询所有包名
            exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/bundleIds",
                    HttpMethod.GET,this.httpEntity, String.class);
            json = new ObjectMapper().readTree(exchange.getBody()).get("data");
            //删除所有包名
            for (JsonNode obj:json){
                restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/bundleIds/"+ obj.get("id").asText(),
                        HttpMethod.DELETE,this.httpEntity, String.class);
            }
        }catch (Exception e){
            log.info("删除证书错误" + e.getMessage());
        }
    }

    /**
     * 创建p12证书返回p12路径和证书id
     * @param directoryPath
     * @param keyPath
     * @param password
     * @return
     * @throws IOException
     */
    public Map<String,String> createCert(String directoryPath,String keyPath,String password) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> formEntity = new HttpEntity<String>(this.certificates, headers);
        ResponseEntity<String> exchange =
                restTemplate.postForEntity("https://api.appstoreconnect.apple.com/v1/certificates",formEntity,String.class);
        //序列化返回
        String json = new ObjectMapper().readTree(exchange.getBody()).get("data").get("attributes").get("certificateContent").asText();
        //证书的id
        String certId = new ObjectMapper().readTree(exchange.getBody()).get("data").get("id").asText();
        //写出证书
        String cerPath =  directoryPath + File.separator + "cer.cer";
        String pemPath =  directoryPath + File.separator + "pem.pem";
        String p12Path =  directoryPath + File.separator + "p12.p12";
        byte[] data = Base64.getDecoder().decode(json);
        IoHandler.fileWriteTxt(cerPath, data);
        //拿pem
        RuntimeExec.runtimeExec("openssl x509 -in " + cerPath  + " -inform DER -outform PEM -out " + pemPath);
        //拿p12
        RuntimeExec.runtimeExec("openssl pkcs12 -export -inkey " +  keyPath + " -in " + pemPath + " -out " + p12Path + " -passout pass:" + password);
        //删除多余文件
        // IoHandler.deleFile(cerPath);
        //IoHandler.deleFile(pemPath);
        Map<String,String> map = new HashMap<String, String>();
        map.put("p12", p12Path);
        map.put("certId",certId);
        return map;
    }


    /**
     * 添加设备
     * @param uuid
     * @return 返回id
     */
    public String addUuid(String uuid){
        Long time = System.currentTimeMillis();
        String json = "{\"data\":{\"type\":\"devices\",\"attributes\":{\"name\":\"replace\",\"platform\":\"IOS\",\"udid\":\"replace\"}}}";
        json = json.replace("replace", uuid);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer " + token);
        String data;
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
            ResponseEntity<String> exchange =
                    restTemplate.postForEntity("https://api.appstoreconnect.apple.com/v1/devices",formEntity,String.class);
            log.info("添加设备:" + (System.currentTimeMillis() - time)/1000 + "秒");

            return  new ObjectMapper().readTree(exchange.getBody()).get("data").get("id").asText();
        }catch (Exception e){
            System.out.println(e.toString());
            if(e.toString().indexOf("ENTITY_ERROR.ATTRIBUTE.INVALID") != -1){
                return "no";
            }else {
                return null;
            }

        }
    }


    /**
     * 获取所有设备
     * @return
     */
    public String queryDevices(){
        Long time = System.currentTimeMillis();
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        this.httpEntity = new HttpEntity(requestHeaders);

        try {
            //执行
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/devices",
                    HttpMethod.GET,httpEntity, String.class);
            log.info("获取所有设备:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return exchange.getBody();
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }

    /**
     * 获取设备指定udid的id
     * @return no不存在,null为证书可能失效
     */
    public String queryDevice(String udid){
        Long time = System.currentTimeMillis();
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        this.httpEntity = new HttpEntity(requestHeaders);
        try {
            //执行
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/devices?filter[udid]=" + udid,
                    HttpMethod.GET,httpEntity, String.class);
            log.info("获取指定设备耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
            JsonNode json = new ObjectMapper().readTree(exchange.getBody()).get("data");
            if(json.size() == 0){
                return null;
            }else {
                log.info(json.get(0).get("id").asText());
                return json.get(0).get("id").asText();
            }
        }catch (Exception e){
            log.info(e.toString());
            return null;
        }
    }



    /**
     * 注册profilesf
     * @param bundleId identifier的id
     * @param certId
     * @param devicesId
     * @param name
     * @param path
     * @return 返回路径
     */
    public Map<String,String> addProfiles(String bundleId,String certId,String devicesId,String name,String path){
        Long time = System.currentTimeMillis();
        Map<String,String> map = new HashMap<>();
        String json = "{\"data\":{\"type\":\"profiles\",\"relationships\":{\"bundleId\":{\"data\":{\"type\":\"bundleIds\",\"id\":\"bundleIdsRep\"}},\"devices\":{\"data\":[{\"type\":\"devices\",\"id\":\"devicesRep\"}]},\"certificates\":{\"data\":[{\"type\":\"certificates\",\"id\":\"certificatesRep\"}]}},\"attributes\":{\"profileType\":\"IOS_APP_ADHOC\",\"name\":\"nameRep\"}}}";
        json = json.replace("bundleIdsRep", bundleId);
        json = json.replace("certificatesRep", certId);
        json = json.replace("devicesRep", devicesId);
        json = json.replace("nameRep", name);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer " + token);
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
            ResponseEntity<String> exchange =
                    restTemplate.postForEntity("https://api.appstoreconnect.apple.com/v1/profiles",formEntity,String.class);
            //获取证书的base64
            String base64 = new ObjectMapper().readTree(exchange.getBody()).get("data").get("attributes").get("profileContent").asText();
            //写出路径
            byte[] data = Base64.getDecoder().decode(base64);
            String filePath = path + "/" + new Date().getTime() + ".mobileprovision";
            IoHandler.fileWriteTxt(filePath, data);
            map.put("filePath",filePath);
            map.put("id",new ObjectMapper().readTree(exchange.getBody()).get("data").get("id").asText());
            log.info("注册profiles耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
            return null;
        }
    }

    /**
     * 创建包名Bundle id返回id
     * @return
     */
    public String addIdentifiers(String identifier,String name){
        Long time = System.currentTimeMillis();
        String json = "{\"data\":{\"type\":\"bundleIds\",\"attributes\":{\"identifier\":\"identifierRep\",\"name\":\"nameRep\",\"platform\":\"IOS\"}}}";
        json = json.replace("identifierRep",identifier);
        json = json.replace("nameRep",name);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer " + token);
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
            ResponseEntity<String> exchange =
                    restTemplate.postForEntity("https://api.appstoreconnect.apple.com/v1/bundleIds",formEntity,String.class);
            log.info("创建包名Bundle 耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
            String bulidd = new ObjectMapper().readTree(exchange.getBody()).get("data").get("id").asText();
            if(null != addBulidFun(bulidd)){
                return bulidd;
            }else {
                return null;
            }
            //return new ObjectMapper().readTree(exchange.getBody()).asText();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }


    /**
     * 新增bulidid功能
     * @return
     */
    public String addBulidFun(String bulidd){
        Long time = System.currentTimeMillis();
        String json = "{\"data\":{\"attributes\":{\"capabilityType\":\"PUSH_NOTIFICATIONS\",\"settings\":[]},\"relationships\":{\"bundleId\":{\"data\":{\"id\":\"idRep\",\"type\":\"bundleIds\"}}},\"type\":\"bundleIdCapabilities\"}}";
        json = json.replace("idRep",bulidd);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer " + token);
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
            ResponseEntity<String> exchange =
                    restTemplate.postForEntity("https://api.appstoreconnect.apple.com/v1/bundleIdCapabilities",formEntity,String.class);
            log.info("创建包名Bundle功能 耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return  new ObjectMapper().readTree(exchange.getBody()).get("data").get("id").asText();
            //return new ObjectMapper().readTree(exchange.getBody()).asText();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }


    /**
     * 查询所有Profiles返回查询结果
     * @return
     */
    public String queryProfiles(){
        try {
            Long time = System.currentTimeMillis();
            //设置请求头参数
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Bearer " + token);
            this.httpEntity = new HttpEntity(requestHeaders);
            //执行
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/profiles",
                    HttpMethod.GET,httpEntity, String.class);
            log.info("查询所有Profiles:" + (System.currentTimeMillis() - time)/1000 + "秒");
            return exchange.getBody();
        }catch (Exception e){
            return null;

        }
    }


    /**
     * 修改设备状态
     * @return 返回id
     */
    public String updateDeviceStatus(String id,String udid,String status){

        Long time = System.currentTimeMillis();
        String json = "{\"data\": {\"id\": \"idRep\",\"type\": \"devices\",\"attributes\": {\"name\": \"nameRep\",\"status\": \"statusRep\"}}}";
        json = json.replace("idRep",id);
        json = json.replace("nameRep",udid);
        json = json.replace("statusRep",status);
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url("https://api.appstoreconnect.apple.com/v1/devices/" + id)
                .patch(RequestBody.create(mediaType, json))
                .addHeader("Authorization",token)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                return  new ObjectMapper().readTree(response.body().string()).get("data").get("id").asText();
            }
        }catch (Exception e){
            log.info(e.toString());
        }
        return null;
    }

    /**
     * 删除配置文件
     * @return 返回success
     */
    public String deleProfiles(String id){
        Long time = System.currentTimeMillis();
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        this.httpEntity = new HttpEntity(requestHeaders);
        try {
            //执行
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/profiles/" + id,
                    HttpMethod.DELETE,httpEntity, String.class);
            log.info("删除配置文件耗费:" + (System.currentTimeMillis() - time)/1000 + "秒");
            System.out.println(exchange.getStatusCode().value());
            log.info("删除配置文件" + exchange.getStatusCode().value());
            if(exchange.getStatusCode().value() == 204){
                return "success";
            }
        }catch (Exception e){
            log.info(e.toString());
        }
        return null;
    }


    /**
     * 请求失败返回null否则返回json
     * @param p12Path
     * @param password
     * @return
     * @throws IOException
     */
    public static String certVerify(String p12Path,String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("p12",p12Path,
                        RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"),
                                new File(p12Path)))
                .addFormDataPart("password", password)
                .build();
        Request request = new Request.Builder()
                .url("https://check.signstack.cc:2052/checkcert")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();


        if(response.code() == 200){
            String res = response.body().string();
            log.info("证书结果" + res);
            return  res;
        }else {
            log.info("证书验证发送请求失败" + response.body().string());
            return  null;
        }


    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIis() {
        return iis;
    }

    public void setIis(String iis) {
        this.iis = iis;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getP8() {
        return p8;
    }

    public void setP8(String p8) {
        this.p8 = p8;
    }
}
