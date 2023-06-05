package com.tyl.waimai.controller;

import com.tyl.waimai.common.Result;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
      @Value("${ruiji.path}")
      private String basePath;
      //文件上传 特定的传参类型 MultipartFile
      @PostMapping("/upload")
      public Result<String> update(@RequestParam("file") MultipartFile file){
          //获取之前文件的后缀名
          String originalFilename = file.getOriginalFilename();
          String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

          //生成新的文件名
          String fileName = UUID.randomUUID()+suffix;

          //判断是否存在目录
          File dir=new File(basePath);
          if(!dir.exists()){
              dir.mkdirs();
          }
          //接收图片 并且指定保存路径
          try {
              file.transferTo(new File(basePath+fileName));
              return Result.success(fileName,"success");
          } catch (IOException e) {
              e.printStackTrace();
          }
          return null;
      }
      //文件的下载
      @GetMapping("download")
      public void download(String name,HttpServletResponse response){
          //需要读的文件
          File file=new File(basePath+name);
          try {
              FileInputStream fileInputStream=new FileInputStream(file);
              ServletOutputStream servletOutputStream=response.getOutputStream();

              response.setContentType("image/jpeg");

              int len=0;
              byte[] b=new byte[1024];

              while((len=fileInputStream.read(b))!=-1){
                 servletOutputStream.write(b,0,len);
                 servletOutputStream.flush();
             }

              fileInputStream.close();
              servletOutputStream.close();

          } catch (Exception e) {
              e.printStackTrace();
          }
      }
}
