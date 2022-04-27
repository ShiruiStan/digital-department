package com.atcdi.digital.service;

import com.atcdi.digital.dao.FileDao;
import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.UploadFile;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Resource
    FileDao fileDao;
    @Resource
    SessionHandler sessionHandler;
    @Resource
    ObjectMapper mapper;
    @Resource
    ProjectService projectService;

    public UploadFile getFileById(int fileId, String token) {
        if (sessionHandler.checkUserDownload(token)) {
            UploadFile file = fileDao.getFileById(fileId);
            if (null == file) {
                throw new StandardException(404, "请求的文件不存在");
            } else {
                return file;
            }
        } else {
            throw new StandardException(403, "您没有下载该文件的权限");
        }
    }

    public List<JsonNode> getAllFiles() {
        List<JsonNode> res = new ArrayList<>();
        List<UploadFile> files = fileDao.getAllFiles();
        files.forEach(file -> {
            ObjectNode node = mapper.valueToTree(file);
            Integer projectId = fileDao.getFileProjectId(file.getFileId());
            if (projectId != null) node.put("projectId", projectId);
            res.add(node);
        });
        return res;
    }

    public void deleteFile(int fileId){
        Integer projectId = fileDao.getFileProjectId(fileId);
        if (projectId != 0){
            projectService.deleteProjectFile(fileId, projectId);
        }else{
            // TODO 通用文件的权限判断
        }
    }

}
