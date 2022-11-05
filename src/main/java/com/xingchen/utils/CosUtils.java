package com.xingchen.utils;

/**
 * @author Li
 * @Date 2022/7/3 1:13
 */
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CosUtils {

    private static String secretId = "AKIDoSv83i6D3cvwg8KFuIPGp77rJUO1NRJw";
    private static String secretKey = "PYQ1LPCiyxf5F71jBPN6ZnlTv85mDOK0";
    private static String bucketName = "lmy-1311156074";//bucket名称
    private static String regionName = "ap-nanjing";  //地区
    private static String baseUrl = "https://lmy-1311156074.cos.ap-nanjing.myqcloud.com"; //cos基本地址
    private static String appId = "lhkp-0vjgrv6s";

    static TransferManager transferManager = null;

    //获取cosClient
    public static COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        transferManager = new TransferManager(cosClient, threadPool);
        return cosClient;
    }

    //上传
    public static String  uploadCos(String type,MultipartFile localFile) {
        String fileName = localFile.getOriginalFilename();
        try{
            String substring = fileName.substring(fileName.lastIndexOf("."));
            File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), substring);
            localFile.transferTo(file);
            if(!FileTypeUtils.fileType(type, file)){
                return "文件类型错误";}
            COSClient cosClient =getCosClient();
            Random random=new Random();
            fileName=random+fileName;
            PutObjectRequest objectRequest = new PutObjectRequest(bucketName,fileName,file);
            cosClient.putObject(objectRequest);
            cosClient.shutdown();
            file.delete();
            return baseUrl+"/"+fileName;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //下载
    public static void downLoadFile() {
        COSClient cosClient =getCosClient();
        // bucket名需包含appid
//        String bucket = "zs-1259422979";
        String key = "/cjrh/img/1111.png";
        File downloadFile = new File("d:/1-4.png");
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        try {
            Download download = transferManager.download(getObjectRequest, downloadFile);
            download.waitForCompletion();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        transferManager.shutdownNow();
        cosClient.shutdown();
    }
    //删除文件
    public static void deleteObject(String url){
        String[] split = url.split("/");
        String key=split[split.length-1];
        COSClient cosClient =getCosClient();
        cosClient.deleteObject(bucketName, key);
        cosClient.shutdown();
    }
}

