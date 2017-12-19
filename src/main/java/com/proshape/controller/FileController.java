package com.proshape.controller;

import com.proshape.domain.File;
import com.proshape.domain.Model;
import com.proshape.domain.User;
import com.proshape.service.FileService;
import com.proshape.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by Katarzyna on 2017-10-20.
 */
@RestController
@RequestMapping("api/file")
public class FileController {

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;


    @Transactional
    @PostMapping(value = "/upload")
    public int uploadObject(@RequestParam("file") List<MultipartFile> files,
                            @RequestParam("fileGroupName") String fileGroupName,
                            @RequestParam(value = "description", required = false) String description) throws IOException, SQLException {
        try{
            fileService.saveUploadedFiles(files, fileGroupName, description);
        } catch (IOException e){
            return 0;
        }

        return 1;
    }

    @Transactional
    @PostMapping(value = "/pictureUpload")
    public void uploadPicutre(@RequestParam("id") Long id, @RequestParam("picture") MultipartFile picture)throws IOException, SQLException {

        fileService.saveModelPicture(id, picture.getBytes());
    }

    @GetMapping(value= "/getObject")
    public byte[] getObject(@RequestParam("fileName")String fileName, @RequestParam("author")String id){
        try{
            byte[] data = fileService.getObject(fileName, Long.parseLong(id));
            return data;
        } catch (IOException e){
            return null;
        }
    }

    @GetMapping(value= "/getMaterial")
    public byte[] getMaterial(@RequestParam("fileName")String fileName, @RequestParam("author")String id){
        try{
            byte[] data = fileService.getObject(fileName, Long.parseLong(id));
            return data;
        } catch (IOException e){
            return null;
        }
    }


    @GetMapping(value= "/getTexture")
    public byte[] getTexture(@RequestParam("fileName")String fileName, @RequestParam("author")String id) {
        try {
            byte[] data = fileService.getObject(fileName, Long.parseLong(id));
            return data;
        } catch (IOException e) {
            return null;
        }
    }

    @GetMapping(value = "/getFileNames")
    public List<String> getFileNames(@RequestParam("id")Long modelId){
        return fileService.getFileNamesForModelId(modelId);
    }

    @GetMapping(value= "getModelById")
    public Model getModelById(@RequestParam("id") Long modelId){
        Model model = fileService.findModelById(modelId);
        model.setFiles(fileService.getFilesForModelId(modelId));
        return model;
    }

    @GetMapping(value = "/getUserObjects")
    public Set<Model> getUserObjects(){
        User user = userService.getUserWithAuthorities();
        return fileService.getModels(user.getId());
    }

    @GetMapping(value = "/getRank")
    public List<Model> getRank(){
        return fileService.getAllModels();
    }

    @PostMapping("/deleteModel")
    public void deleteModel(@RequestParam("modelId") String modelId){
        fileService.deleteModel(Long.parseLong(modelId));
    }



    @PostMapping("/updateModel")
    public void updateModel(@RequestParam("modelId") Long modelId,
                            @RequestParam("modelName") String modelName,
                            @RequestParam("modelDescription") String modelDescription,
                            @RequestParam("categoryId") Long categoryId){
        fileService.updateModel(modelId, modelName, modelDescription, categoryId);
    }
}
