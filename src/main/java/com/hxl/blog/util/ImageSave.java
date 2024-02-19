package com.hxl.blog.util;

import com.hxl.blog.pojo.Configure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Create By hxl on 2021/4/9
 */
@Service
public class ImageSave {
    @Autowired
    private Configure configure;
    public List<String> utils(HttpServletRequest req, MultipartFile file) {
        String url = new String(req.getRequestURL());
        String virtualPath = null;
        ArrayList<String> list = new ArrayList<>();
        url = url.substring(0, url.lastIndexOf(req.getServletPath()));
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if ("image/jpeg".equalsIgnoreCase(contentType) || "image/jpg".equalsIgnoreCase(contentType) || "image/png".equalsIgnoreCase(contentType)) {
                String fileName = file.getOriginalFilename();
                fileName = UUID.randomUUID().toString().replaceAll("-", "") + fileName.substring(fileName.lastIndexOf("."));
                String visImageUrl = configure.getVisImageUrl();
                String imageUrl = configure.getImageUrl();
                imageUrl = imageUrl.replaceAll("file:", "");
                visImageUrl = visImageUrl.replaceAll("\\*\\*", "");
                virtualPath = url + visImageUrl + fileName;

                String savePath = imageUrl + fileName;  //图片保存路径
                list.add(savePath);
                File saveFile = new File(savePath);
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }
                try {
                    file.transferTo(saveFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        list.add(virtualPath);
        return list;
    }

    public static boolean deleteImg(String path){
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + path + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + path + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + path + "不存在！");
            return false;
        }
    }
}
