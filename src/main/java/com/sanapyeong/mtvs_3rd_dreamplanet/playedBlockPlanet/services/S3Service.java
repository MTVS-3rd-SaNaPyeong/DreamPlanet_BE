package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final AdditionalModelsConverter additionalModelsConverter;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public S3Service(
            AmazonS3Client amazonS3Client,
            AdditionalModelsConverter additionalModelsConverter){
        this.amazonS3Client = amazonS3Client;
        this.additionalModelsConverter = additionalModelsConverter;
    }

    private final String COLOR_DOT_IMAGE_DIR = "color-dot-image/";
    private final String BLACK_AND_WHITE_DOT_IMAGE_DIR = "black-and-white-dot-image/";
    private final String COMPLETED_WORK_DIR = "completed-work/";

    public String saveCompleteWork(MultipartFile multipartFile, Long playedBlockPlanetId) throws IOException{

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, COMPLETED_WORK_DIR, playedBlockPlanetId);
    }

    public String saveColorDotImage(MultipartFile multipartFile, Long playedBlockPlanetId) throws IOException{

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, COLOR_DOT_IMAGE_DIR, playedBlockPlanetId);
    }

    public String saveBlackAndWhiteDotImage(MultipartFile multipartFile, Long playedBlockPlanetId) throws IOException{

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, BLACK_AND_WHITE_DOT_IMAGE_DIR, playedBlockPlanetId);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName, Long playedBlockPlanetId) {
        String fileName = playedBlockPlanetId + "/" + dirName + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

//    // S3로 업로드
//    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
//                CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File("C:\\dev" + "\\" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public void createDir(String bucketName, String folderName) {
        amazonS3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }
}
