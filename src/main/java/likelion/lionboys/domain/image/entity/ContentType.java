package likelion.lionboys.domain.image.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ContentType {

    JPEG("image/jpeg"),
    PNG("image/png"),
    WEBP("image/webp"),
    GIF("image/gif");

    private final String mimeType;

    /**
     * MIME 타입 문자열로부터 enum 찾기
     */
    public static ContentType fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(type -> type.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "지원하지 않는 이미지 타입입니다: " + mimeType
                ));
    }

    /**
     * 파일 확장자로부터 enum 찾기
     */
    public static ContentType fromFilename(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();

        return switch (extension) {
            case ".jpg", ".jpeg" -> JPEG;
            case ".png" -> PNG;
            case ".webp" -> WEBP;
            case ".gif" -> GIF;
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 파일 확장자입니다: " + extension
            );
        };
    }
}