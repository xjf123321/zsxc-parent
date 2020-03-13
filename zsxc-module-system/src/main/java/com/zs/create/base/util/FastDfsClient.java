package com.zs.create.base.util;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.IniFileReader;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * @author hy
 * @ClassName: FastDFSClientWrapper
 * @Description: fastDFS客户端包装类
 * @date 2018年6月3日 上午12:10:40
 */
@Slf4j
public class FastDfsClient {
    private static TrackerClient trackerClient = null;
    private static TrackerServer trackerServer = null;
    private static StorageServer storageServer = null;
    private static StorageClient storageClient = null;
    private static final String SEPARATOR = "/";
    private static final String PROTOCOL = "http://";
    private static String ngnixAddr;

    static {
        try {
            URL dfsUrl = FastDfsClient.class.getClassLoader().getResource("fdfs_client.conf");
            if (dfsUrl == null) {
                throw new Exception("can not find fdfs_client.conf");
            }
            File clientFile = new File(dfsUrl.toURI());
            String filePath = clientFile.getAbsolutePath();
            ClientGlobal.init(filePath);
            ngnixAddr = new IniFileReader(filePath).getStrValue("ngnixAddr");
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            log.error("FastDFS Client Init Fail!", e);
        }

    }

    /**
     * @param @param  fileName 文件名称
     * @param @param  content 内容
     * @param @param  author 作者
     * @param @param  ext  扩展名
     * @param @return 设定文件
     * @return String[]   返回类型
     * @throws
     * @Title: upload
     * @Description: 文件上传 ： 使用FastDFS提供的客户端storageClient来进行文件上传，最后将上传结果返回
     */
    public static String[] upload(String fileName, byte[] content, String author, String ext) {
        log.info("Excel Name: " + fileName + "Excel Length:" + content.length);
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("author", author);
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            //storageClient = new StorageClient(trackerServer, storageServer);
            uploadResults = storageClient.upload_file(content, ext, metaList);
        } catch (IOException e) {
            log.error("IO Exception when uploadind the file:" + fileName, e);
        } catch (Exception e) {
            log.error("Non IO Exception when uploadind the file:" + fileName, e);
        }
        log.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");
        if (uploadResults == null) {
            log.error("upload file fail, error code:" + storageClient.getErrorCode());
            return null;
        }
        log.info(
                "upload file successfully!!!" + "group_name:" + uploadResults[0] + ", remoteFileName:" + " " + uploadResults[1]);
        return uploadResults;
    }

    /**
     * @param @param  file
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: uploadRtnPath
     * @Description: 上传并返回路径
     */
    public static String uploadRtnPath(String fileName, byte[] content, String author, String ext) {
        log.info("Excel Name: " + fileName + "Excel Length:" + content.length);
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("author", author);
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            //解决fastdfsjar包的并发问题
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            uploadResults = storageClient.upload_file(content, ext, metaList);
        } catch (IOException e) {
            log.error("IO Exception when uploadind the file:" + fileName, e);
        } catch (Exception e) {
            log.error("Non IO Exception when uploadind the file:" + fileName, e);
        }
        log.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");
        if (uploadResults == null) {
            log.error("upload file fail, error code:" + storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];
        if (ngnixAddr == null) {
            log.error("upload file fail, ngix_addr is null");
            return null;
        }
        String fileAbsolutePath = PROTOCOL
                + ngnixAddr
                + SEPARATOR + groupName
                + SEPARATOR + remoteFileName;
        log.info(
                "upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        return fileAbsolutePath;
    }

    /**
     * @param @param  groupName
     * @param @param  remoteFileName
     * @param @return 设定文件
     * @return FileInfo    返回类型
     * @throws
     * @Title: getFile
     * @Description: 根据groupName和文件名获取文件信息
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            log.error("IO Exception: Get Excel from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get Excel from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * @param @param  groupName
     * @param @param  remoteFileName
     * @param @throws Exception    设定文件
     * @return void    返回类型
     * @throws
     * @Title: deleteFile
     * @Description: 删除文件
     */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception {
        storageClient = new StorageClient(trackerServer, storageServer);
        int i = storageClient.delete_file(groupName, remoteFileName);
        log.info("delete file successfully!!!" + i);
    }


    /**
     * @param @param  groupName
     * @param @param  remoteFileName
     * @param @return 设定文件
     * @return InputStream    返回类型
     * @throws
     * @Title: downFile
     * @Description: 下载文件
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        InputStream ins = null;
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            ins = new ByteArrayInputStream(fileByte);
        } catch (IOException e) {
            log.error("IO Exception: Get Excel from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get Excel from Fast DFS failed", e);
        }
        return ins;
    }

}
