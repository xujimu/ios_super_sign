package com.wlznsb.iossupersign.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.odyssi.asc4j.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

/**
 *
 * 访问苹果api的工具类
 *
 */
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
            this.token = TokenUtil.generateToken(this.iis, this.kid,this.p8);
            System.out.println(this.token);
            //设置请求头参数
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Bearer " + token);
            this.httpEntity = new HttpEntity(requestHeaders);
            //执行
            ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/apps",
                    HttpMethod.GET,httpEntity, String.class);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 删除所有证书
     * @throws JsonProcessingException
     */
    public void deleCertAll() throws JsonProcessingException {
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
        String json = "{\"data\":{\"type\":\"devices\",\"attributes\":{\"name\":\"replace\",\"platform\":\"IOS\",\"udid\":\"replace\"}}}";
        json = json.replace("replace", uuid);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Authorization", "Bearer " + token);
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
            ResponseEntity<String> exchange =
                    restTemplate.postForEntity("https://api.appstoreconnect.apple.com/v1/devices",formEntity,String.class);
            return  new ObjectMapper().readTree(exchange.getBody()).get("data").get("id").asText();
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }


    /**
     * 获取所有设备
     * @return
     */
    public String queryDevices(){
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        this.httpEntity = new HttpEntity(requestHeaders);
        //执行
        ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/devices",
                HttpMethod.GET,httpEntity, String.class);
        return exchange.getBody();
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
    public String addProfiles(String bundleId,String certId,String devicesId,String name,String path){
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
            return filePath;
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
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer " + token);
        this.httpEntity = new HttpEntity(requestHeaders);
        //执行
        ResponseEntity<String> exchange = restTemplate.exchange("https://api.appstoreconnect.apple.com/v1/profiles",
                HttpMethod.GET,httpEntity, String.class);
        return exchange.getBody();
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
