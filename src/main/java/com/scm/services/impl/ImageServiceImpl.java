package com.scm.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.Exception.AppConstant;
import com.scm.services.ImageService;
@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;
    

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    @Override
    public String uploadImage(MultipartFile contactimage,String filename) {

        

        try {
            byte[] data= new byte[contactimage.getInputStream().available()];


            contactimage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id",filename
            ));
            return this.getUrlFromPublicId(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
       
  
    }


    @Override
    public String getUrlFromPublicId(String public_id) {
        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstant.CONTACT_IMAGE_WIDTH)
            .height(AppConstant.CONTACT_IMAGE_WIDTH)
            .crop(AppConstant.CONTACT_IMAGE_CROP)
        )
        .generate(public_id);
        
    }

}
