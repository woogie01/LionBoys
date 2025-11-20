package likelion.lionboys.domain.image.exception;

import likelion.lionboys.domain.image.exception.error.ImageErrorCode;
import likelion.lionboys.global.exception.CustomException;

public class ImageException extends CustomException {
    public ImageException(ImageErrorCode code) {
        super(code);
    }
    public ImageException(ImageErrorCode code,  String message) {
        super(code, message);
    }

    public static ImageException imageNotFound(Long roundId) {
        return new ImageException(
                ImageErrorCode.IMAGE_NOT_FOUND,
                "해당 라운드(" + roundId + ")의 이미지를 찾을 수 없습니다."
                );
    }

    public static ImageException invalidextension(String extention) {
        return new ImageException(
                ImageErrorCode.INVALID_IMAGE_TYPE,
                "해당 타입(" + extention + ")은 잘못된 이미지 타입입니댜."
        );
    }

    public static ImageException invalidMimeType(String mimeType) {
        return new ImageException(
                ImageErrorCode.INVALID_IMAGE_TYPE, // (에러 코드 예시)
                "지원하지 않는 MIME 타입입니다: " + mimeType
        );
    }
}
