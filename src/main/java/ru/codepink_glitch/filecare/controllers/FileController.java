package ru.codepink_glitch.filecare.controllers;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.codepink_glitch.filecare.dto.FileDisplay;
import ru.codepink_glitch.filecare.dto.FolderDetails;
import ru.codepink_glitch.filecare.services.FileService;

import java.util.List;

@RestController
@RequestMapping("/browse")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    FileService fileService;

    @GetMapping("/initial")
    public ResponseEntity<List<FileDisplay>> getInitialFolder(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(fileService.getInitialInFolder(userDetails), HttpStatus.OK);
    }

    @PostMapping("/newFolder")
    public ResponseEntity<Boolean> createDirectory(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody FolderDetails folderDetails) {
        return new ResponseEntity<>(fileService.createFolder(folderDetails, userDetails), HttpStatus.OK);
    }

    @PostMapping("/folder")
    public ResponseEntity<List<FileDisplay>> getFolder(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody @NonNull FolderDetails folderDetails) {
        return new ResponseEntity<>(fileService.getAllInFolder(folderDetails, userDetails), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> uploadFile(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam("folderDetails") String folderDetails,
                                              @RequestParam("file") MultipartFile file) {
//        return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(fileService.persistFile(userDetails, folderDetails, file), HttpStatus.OK);
    }
}
