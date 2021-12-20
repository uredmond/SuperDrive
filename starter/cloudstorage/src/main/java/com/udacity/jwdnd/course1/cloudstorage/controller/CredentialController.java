package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private CredentialService credentialService;

    @PostMapping()
    public String addOrUpdateCredential(@ModelAttribute Credential credential, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        int userId = user.getUserId();
        Integer credentialId = credential.getCredentialId();
        if (credentialId != null && credentialId > 0) {
            credentialService.updateCredential(credential, userId);
        } else {
            int rowsAdded = credentialService.insertCredential(credential, userId);
            if (rowsAdded <= 0) {
                return "redirect:/result?error";
            }
        }
        logCredentials("addOrUpdateCredential");
        return "redirect:/result?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable int id) {
        logger.info("Requesting delete for credentialId: " + id);
        if (id > 0) {
            int rowsDeleted = credentialService.deleteCredential(id);
            if (rowsDeleted <= 0) {
                return "redirect:/result?error";
            }
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }

    private void logCredentials(String methodName) {
        logger.info(methodName);
        List<Credential> credentials = credentialService.getAllCredentials();
        for (Credential credential : credentials) {
            logger.info(credential.toString());
        }
    }

}
