package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.NestedServletException;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.apache.tomcat.util.http.fileupload.IOUtils.copy;

@Controller
@RequestMapping("/file")
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @PostMapping("file-upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        int userId = user.getUserId();
        String fileName = fileUpload.getOriginalFilename();
        if (fileName == null || fileName.equals("")) {
            return "redirect:/result?missing";
        }
        if (fileService.getFile(fileName) != null) {
            return "redirect:/result?error";
        }
        int rowsAdded = fileService.insertFile(fileUpload, userId);
        if (rowsAdded <= 0) {
            return "redirect:/result?error";
        }
        logFiles("handleFileUpload");
        return "redirect:/result?success";
    }

    @GetMapping("/{name}")
    public String getFile(@PathVariable String name, Model model) {
        model.addAttribute("file", fileService.getFile(name));
        logFiles("getFile");
        return "result";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable int id) {
        int rowsDeleted = fileService.deleteFile(id);
        if (rowsDeleted < 0) {
            return "redirect:/result?error";
        }
        logFiles("deleteFile");
        return "redirect:/result?success";
    }

    @GetMapping("/download/{name}")
    public void downloadFile(@PathVariable String name, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
        File file = fileService.getFile(name);
        InputStream is = file.getFileData();
        copy(is, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class, SizeLimitExceededException.class, NestedServletException.class})
    public String handleFileUploadError(RedirectAttributes ra){
        ra.addFlashAttribute("status","Your file exceeds the allowed size of 5MB!");
        return "redirect:/home";
    }

    private void logFiles(String methodName) {
        logger.info(methodName);
        List<File> files = fileService.getAllFiles();
        for (File file : files) {
            logger.info(file.toString());
        }
    }
}
