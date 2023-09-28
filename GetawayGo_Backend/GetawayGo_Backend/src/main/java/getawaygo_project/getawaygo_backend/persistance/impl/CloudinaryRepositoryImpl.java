package getawaygo_project.getawaygo_backend.persistance.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import getawaygo_project.getawaygo_backend.business.exception.FileException;
import getawaygo_project.getawaygo_backend.persistance.CloudinaryRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class CloudinaryRepositoryImpl implements CloudinaryRepository {
    @Override
    public List<String> uploadPictures(List<MultipartFile> photos) {
        //here we upload the picture to Cloudinary and save the link in the database
        Map config = new HashMap();
        config.put("cloud_name", "dgqq1qtef");
        config.put("api_key", "163684377281945");
        config.put("api_secret", "_6HyUYuHa_1D3Wurqyo5hb1x-_E");
        Cloudinary cloudinary = new Cloudinary(config);

        List<String> imageUrls = new ArrayList<>();

        try {
            for (MultipartFile photo : photos) {
                Map uploadResult = cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                imageUrls.add(imageUrl);
            }

            return imageUrls;
        } catch (IOException exception) {
            throw new FileException();
        }
    }

}
