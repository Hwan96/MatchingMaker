package MatchingMaker.MatchingMaker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswererDTO {
    private String field;
    private String tag;
    private MultipartFile proofFile;

    // 파일 저장 경로를 반환하는 메서드
    public String saveProofFile() {
        if (proofFile == null || proofFile.isEmpty()) { return null; }

        String fileName = StringUtils.cleanPath(proofFile.getOriginalFilename());
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", " ");
        try {
            String directory = "D:/MM/Answerer/";
            Path directoryPath = Paths.get(directory);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(fileName);
            Files.copy(proofFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
