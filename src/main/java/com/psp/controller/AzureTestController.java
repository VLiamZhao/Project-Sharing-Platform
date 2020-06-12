package com.psp.controller;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.psp.service.AzureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
@RestController
@RequestMapping("/azureblob")
public class  AzureTestController {
    @Autowired
    private AzureService azureService;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<URI> upload(@RequestParam("file") MultipartFile file){
        URI url = azureService.upload(file);
        return ResponseEntity.ok().body(url);
    }
}
