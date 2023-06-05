package com.example.onekids_project.request.album;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2021-04-15 13:10
 *
 * @author lavanviet
 */
@Data
public class AlbumCreateRequest {
    @NotBlank
    private String albumName;

    private String albumDescription;

    @NotNull
    private Long idClass;

    @NotEmpty
    private List<MultipartFile> multipartFileList;
}
