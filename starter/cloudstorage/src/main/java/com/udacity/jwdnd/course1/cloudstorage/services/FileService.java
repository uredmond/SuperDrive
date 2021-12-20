package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public Integer insertFile(MultipartFile fileUpload, int userId) throws IOException {
        File file = new File();
        file.setFileName(fileUpload.getOriginalFilename());
        file.setContentType(fileUpload.getContentType());
        file.setFileSize(Long.toString(fileUpload.getSize()));
        file.setFileData(fileUpload.getInputStream());
        file.setUserId(userId);
        return fileMapper.insertFile(file);
    }

    public File getFile(String fileName) {
        return fileMapper.getFile(fileName);
    }

    public List<File> getFiles(int userId) {
        return fileMapper.getFiles(userId);
    }

    public Integer deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public List<File> getAllFiles() {
        return fileMapper.getAllFiles();
    }

}
