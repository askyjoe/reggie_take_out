package com.litj.reggie.controller;

import com.litj.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try{
            file.transferTo(new File(basePath+"/"+fileName));
        } catch(Exception e){
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    @GetMapping("download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream fis = new FileInputStream(new File(basePath+name));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = fis.read(buffer)) != -1)
            {
                outputStream.write(buffer,0,len);
                outputStream.flush();
            }
            fis.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
