package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.LocalCredential;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class CredentialService {

    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private EncryptionService encryptionService;

    public Integer insertCredential(Credential credential, int userId) {
        credential.setUserId(userId);
        setEncryptedFields(credential);
        return credentialMapper.insertCredential(credential);
    }

    public void updateCredential(Credential credential, int userId) {
        credential.setUserId(userId);
        setEncryptedFields(credential);
        credentialMapper.updateCredential(credential);
    }

    public Integer deleteCredential(int credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    public List<LocalCredential> getCredentials(int userId) {
        List<Credential> credentials = credentialMapper.getCredentials(userId);
        List<LocalCredential> localCredentials = new ArrayList<>();
        for (Credential credential : credentials) {
            LocalCredential localCredential = new LocalCredential();
            localCredential.setCredentialId(credential.getCredentialId());
            localCredential.setUrl(credential.getUrl());
            localCredential.setUsername(credential.getUsername());
            localCredential.setKey(credential.getKey());
            localCredential.setPassword(credential.getPassword());
            localCredential.setUserId(credential.getUserId());
            localCredential.setDecryptedPassword(decryptPassword(credential));
            localCredentials.add(localCredential);
        }
        return localCredentials;
    }

    public List<Credential> getAllCredentials() {
        return credentialMapper.getAllCredentials();
    }

    private void setEncryptedFields(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setKey(encodedKey);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
    }

    private String decryptPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

}
