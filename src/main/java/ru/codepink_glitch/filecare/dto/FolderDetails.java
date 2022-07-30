package ru.codepink_glitch.filecare.dto;

import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FolderDetails {

    String folder;
    String folderName;

    public String concatFullPath() {
        return Stream.of(folder, folderName).filter(el -> !Strings.isNullOrEmpty(el)).collect(Collectors.joining("/"));
    }
}
