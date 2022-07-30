package ru.codepink_glitch.filecare.controllers;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
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

    // TODO сделать эндпоинт для просмотра конкретной папки
    @PostMapping("/folder")
    public ResponseEntity<List<FileDisplay>> getFolder(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody @NonNull FolderDetails folderDetails) {
        return new ResponseEntity<>(fileService.getAllInFolder(folderDetails, userDetails), HttpStatus.OK);
    }
}
