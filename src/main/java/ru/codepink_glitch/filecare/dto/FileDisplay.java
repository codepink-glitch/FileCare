package ru.codepink_glitch.filecare.dto;

import com.google.common.io.Files;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.File;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileDisplay {

    String name;
    String extension;
    String path;

    public FileDisplay(File file) {
        this.name = file.getName();
        this.extension = file.isDirectory() ? "directory" : Files.getFileExtension(file.getName());
        this.path = file.getAbsolutePath();
    }
}
