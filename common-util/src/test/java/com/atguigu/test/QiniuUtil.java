package com.atguigu.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiniuUtil {

     @Test
    public void upload(){
         //构造一个带指定 Region 对象的配置类
         Configuration cfg = new Configuration(Zone.zone0());
         UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
         String accessKey = "evHo3hnqWs0G1IxU--ONUfd6roAPcB7CKJKcTvj4";
         String secretKey = "ghEIucwCRVL6vXoYrnt8qNZ1iAbTxB73b7RaAswJ";
         String bucket = "0718shf";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
         String localFilePath = "d://pic//1.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
         String key = null;

         //登录(认证)
         Auth auth = Auth.create(accessKey, secretKey);
         String upToken = auth.uploadToken(bucket);

         try {
             Response response = uploadManager.put(localFilePath, key, upToken);
             //解析上传成功的结果
             DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
             System.out.println(putRet.key);
             System.out.println(putRet.hash);
         } catch (QiniuException ex) {
             Response r = ex.response;
             System.err.println(r.toString());
             try {
                 System.err.println(r.bodyString());
             } catch (QiniuException ex2) {
                 //ignore
             }
         }

     }

     @Test
    public void delete(){
         //构造一个带指定 Region 对象的配置类
         Configuration cfg = new Configuration(Zone.zone0());
         UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
         String accessKey = "evHo3hnqWs0G1IxU--ONUfd6roAPcB7CKJKcTvj4";
         String secretKey = "ghEIucwCRVL6vXoYrnt8qNZ1iAbTxB73b7RaAswJ";
         String bucket = "0718shf";
         String key = "FtPKk_nkkXpiTCWc3wDe6akV1UoV";

         Auth auth = Auth.create(accessKey, secretKey);
         BucketManager bucketManager = new BucketManager(auth, cfg);
         try {
             bucketManager.delete(bucket, key);
         } catch (QiniuException ex) {
             //如果遇到异常，说明删除失败
             System.err.println(ex.code());
             System.err.println(ex.response.toString());
         }

     }
}
