package com.atcdi.digital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Data
public class UploadFile {

    int fileId;
    String fileName;
    String filePath = "/";
    boolean byteDownload = false;

    public boolean needSaveInDatabase(String uploadPath, MultipartFile file) throws IOException {
        File folder = new  File(uploadPath + filePath);
        if (!folder.exists() && !folder.mkdirs()) return false;
        File f = new File(uploadPath + filePath + fileName);
        if (f.exists()) {
            file.transferTo(f);
            return false;
        }else if(f.createNewFile()){
            file.transferTo(f);
            return true;
        }else throw new StandardException(500, "文件创建失败 ： " +  filePath + fileName);
    }
}
