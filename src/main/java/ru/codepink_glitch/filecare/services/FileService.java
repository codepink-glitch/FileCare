package ru.codepink_glitch.filecare.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import ru.codepink_glitch.filecare.dto.FileDisplay;
import ru.codepink_glitch.filecare.dto.FolderDetails;
import ru.codepink_glitch.filecare.exceptions.ExceptionsEnum;
import ru.codepink_glitch.filecare.exceptions.ServiceException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * service working with files
 * list files, save file, delete file etc
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

    String PATH;
    ObjectMapper objectMapper;

    public FileService(@Value("${variables.filePath:#{systemProperties['java.io.tmpdir']}}")String path, ObjectMapper objectMapper) {
        this.PATH = initFolder(path).toString();
        this.objectMapper = objectMapper;
    }

    public Path initFolder(String parentFolder) {
        try {
            Path fileCarePath = Path.of(parentFolder + "fileCare");
            if (Files.isDirectory(fileCarePath)) {
                return fileCarePath;
            } else {
                return Files.createDirectory(fileCarePath);
            }
        } catch (IOException e) {
            System.exit(1);
        }
        return null;
    }

    public List<FileDisplay> getInitialInFolder(UserDetails userDetails) {
        String username = userDetails.getUsername();
        if (!isDirectoryExists(PATH + "/" +  username)) {
            try {
                Files.createDirectory(Paths.get(PATH + "/" + username));
            } catch (IOException e) {
                throw new ServiceException(ExceptionsEnum.DIRECTORY_CREATE_EXCEPTION);
            }
        }
        return getAllInFolder(new FolderDetails(null, null), userDetails);
    }

    public List<FileDisplay> getAllInFolder(FolderDetails folderDetails, UserDetails userDetails) {
        File directory =
                new File(PATH + "/" + userDetails.getUsername() + "/" + folderDetails.concatFullPath());

        if (!directory.isDirectory() || !directory.exists()) {
            throw new ServiceException(ExceptionsEnum.NOT_A_DIRECTORY);
        }

        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .map(FileDisplay::new)
                .collect(Collectors.toList());
    }

    public byte[] getFile(String path) {
        File file = new File(path);

        if (file.isDirectory() || !file.exists()) {
            throw new ServiceException(ExceptionsEnum.FILE_NOT_FOUND);
        }

        InputStream inputStream = getClass().getResourceAsStream(file.getAbsolutePath());
        if (inputStream != null) {
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                throw new ServiceException(ExceptionsEnum.FILE_DOWNLOAD_EXCETPION);
            }
        }
        return new byte[0];
    }

    public boolean removeFile(String path) {
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            throw new ServiceException(ExceptionsEnum.FILE_NOT_FOUND);
        }
        return targetFile.delete();
    }

    public boolean recursivelyDeleteFolder(String path) {
        File targetFolder = new File(path);
        if (!targetFolder.isDirectory() || !targetFolder.exists())
            throw new ServiceException(ExceptionsEnum.FILE_NOT_FOUND);

        File[] folderFiles = targetFolder.listFiles();
        if (Objects.isNull(folderFiles))
            throw new ServiceException(ExceptionsEnum.FILE_NOT_FOUND);

        for (File file: folderFiles) {
            if (file.isDirectory())
                recursivelyDeleteFolder(file.getAbsolutePath());
            else
                file.delete();
        }

        targetFolder.delete();

        return true;
    }

    public boolean delete(UserDetails userDetails, FileDisplay file) {
        File target = new File(PATH + "/" + userDetails.getUsername() + "/" + file.getPath() + "/" + file.getName());

        if (!target.exists())
            throw new ServiceException(ExceptionsEnum.FILE_NOT_FOUND);
        if (target.isDirectory())
            recursivelyDeleteFolder(target.getAbsolutePath());
        else
            target.delete();

        return true;
    }

    public boolean moveFile(String initialPath, String targetPath) {
        File initialFile = new File(initialPath);
        if (!initialFile.exists()) {
            throw new ServiceException(ExceptionsEnum.FILE_NOT_FOUND);
        }
        return initialFile.renameTo(new File(targetPath + "/" + initialFile.getName()));
    }

    public boolean createFolder(FolderDetails folderDetails, UserDetails userDetails) {

        if (folderDetails.getFolderName() == null) {
            throw new ServiceException(ExceptionsEnum.DIRECTORY_CREATE_EXCEPTION);
        }

        try {
            Files.createDirectory(
                    Paths.get(String.format("%s/%s/%s", PATH, userDetails.getUsername(), folderDetails.concatFullPath())));
        } catch (IOException e) {
            throw new ServiceException(ExceptionsEnum.DIRECTORY_CREATE_EXCEPTION);
        }
        return true;
    }

    public boolean isDirectoryExists(String path) {
        Path of = Path.of(path);
        return Files.exists(of) && Files.isDirectory(of);
    }

    public boolean persistFile(UserDetails userDetails, String folderDetails, MultipartFile multipartFile) {
        try {
            String username = userDetails.getUsername();
            FolderDetails folderDetailsObj = objectMapper.readValue(folderDetails, FolderDetails.class);
            String folder = folderDetailsObj.getFolder();
            File file =
                    new File(PATH + "/" + username + (Strings.isNullOrEmpty(folder) ? "" : "/" + folder) + "/" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
        } catch (Exception e) {
            throw new ServiceException(ExceptionsEnum.FILE_UPLOAD_EXCEPTION);
        }
        return true;
    }

}
