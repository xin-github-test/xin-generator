package com.xin.web.manager;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.xin.web.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Cos 对象存储操作
 *
 * @author <a href="https://github.com/lixin">程序员xin</a>
 * @from <a href="https://xin.icu">编程导航知识星球</a>
 */
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private OSS cosClient;

    /**
     * 上传对象
     *
     * @param key           唯一键
     * @param localFilePath 本地文件路径
     * @return
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                new File(localFilePath));
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传对象
     *
     * @param key  唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 文件的下载
     * @param key
     * @return
     */
    public OSSObject getObject(String key){
        return cosClient.getObject(cosClientConfig.getBucket(), key);
    }

    /**
     * 下载对象到本地文件
     * @param key
     * @param localFilePath
     */
    public void download(String key, String localFilePath) {
        cosClient.getObject(new GetObjectRequest(cosClientConfig.getBucket(), key), new File(localFilePath));
    }

    /**
     * 删除对象
     * @param key
     */
    public void deleteObject(String key) {
        // 5. 删除文件
        cosClient.deleteObject(cosClientConfig.getBucket(), key);
    }

    /**
     * 批量删除文件
     * @param keyList
     * @return
     */
    public DeleteObjectsResult deleteObjects(List<String> keyList){
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket()).withKeys(keyList).withEncodingType("url");
        return cosClient.deleteObjects(deleteObjectsRequest);
    }

    /**
     * 删除指定目录
     * @param delPrefix
     * @return
     */
    public void deleteDir(String delPrefix) throws OSSException, ClientException {
        String nextMarker = null;
        ObjectListing objectListing;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(cosClientConfig.getBucket())
                    .withPrefix(delPrefix)
                    .withMarker(nextMarker);

            objectListing = cosClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<>();
                for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                    System.out.println("key name: " + s.getKey());
                    keys.add(s.getKey());
                }
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket()).withKeys(keys).withEncodingType("url");
                cosClient.deleteObjects(deleteObjectsRequest);
            }

            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
    }
}
