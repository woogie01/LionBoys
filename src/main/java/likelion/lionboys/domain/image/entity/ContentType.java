package likelion.lionboys.domain.image.entity;

import likelion.lionboys.domain.image.exception.ImageException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ContentType {

    // Enum 이름, MIME 타입(신분증), 확장자(이름표)
    JPG("image/jpg", "jpg"),
    JPEG("image/jpeg", "jpg"), // JPG와 JPEG는 둘 다 "jpg" 확장자
    PNG("image/png", "png");
    // WEBP("image/webp", "webp"), GIF("image/gif", "gif") 등 추가 가능

    private final String mimeType;
    private final String extension; // ⬅️ "jpg", "png" 정보

    /**
     * 파일 확장자로부터 enum 찾기
     */
    public static ContentType fromFilename(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();

        return switch (extension) {
            case ".jpg", ".jpeg" -> JPEG;
            case ".png" -> PNG;
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 파일 확장자입니다: " + extension
            );
        };
    }
    /*
    * MIME 타입 문자열로부터 enum 찾기 (유효성 검사)
     *
     * @param mimeType "image/jpeg"와 같은 클라이언트 요청 값
     * @return ContentType.JPEG, ContentType.PNG 등
     * @throws ImageException 지원하지 않는 MIME 타입일 경우
     */
    public static ContentType fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(type -> type.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                // 지원하지 않는 값이면 ImageException 발생
                .orElseThrow(() -> ImageException.invalidMimeType(mimeType));
    }
}