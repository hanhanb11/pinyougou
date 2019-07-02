package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
public class UploadController {
    //获取properties文件中的url路径进行拼接

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;
    @Autowired
    private HttpServletRequest request;


    @RequestMapping("/upload")
    public Result upload(MultipartFile file) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        System.out.println("originalFilename........."+originalFilename);
        //获取扩展名
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        System.out.println(extName+"。。。。。。。。。。。。。。。。。。。。");
        try {
            //客户端对象
            FastDFSClient client= new FastDFSClient("classpath:config/fdfs_client.conf");
            String path = client.uploadFile(file.getBytes(), extName);
            String url = FILE_SERVER_URL + path;//获取上传文件路径

            System.out.println("url>>>>>>>"+url);

            return new Result(true,url);//返回前端文件路径

        } catch (Exception e) {

            e.printStackTrace();

            return new Result(false,"上传失败"+e.getMessage());
        }
    }
}
