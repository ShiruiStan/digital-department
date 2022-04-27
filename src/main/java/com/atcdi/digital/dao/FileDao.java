package com.atcdi.digital.dao;

import com.atcdi.digital.entity.UploadFile;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FileDao {
    @Select("SELECT * FROM files WHERE file_id=#{fileId}")
    UploadFile getFileById(int fileId);

    @Insert("INSERT INTO files (file_name, file_path, byte_download) VALUES (#{fileName},#{filePath},#{byteDownload})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId", keyColumn = "file_id")
    boolean insertNewFile(UploadFile file);

    @Delete("DELETE FROM files WHERE file_id=#{fileId}")
    boolean deleteFileById(int fileId);

    @Insert("INSERT INTO project_file (project_id, file_id) VALUES (#{projectId},#{fileId})")
    boolean addFileToProject(int projectId, int fileId);

    @Delete("DELETE FROM project_file WHERE file_id=#{fileId} and project_id=#{projectId}")
    boolean deleteFileFromProject(int projectId, int fileId);

    @Select("SELECT * FROM files")
    List<UploadFile> getAllFiles();

    @Select("SELECT project_id FROM project_file WHERE file_id = #{fileId}")
    Integer getFileProjectId(int fileId);

    @Select("SELECT file_id FROM files WHERE file_name = #{fileName} and file_path = #{filePath}")
    int getFileId(UploadFile file);





}
