package likelion.lionboys.domain.image.dto.response;

import java.util.List;

public record ImageResp(
        int count,
        List<ImageItem> images
) {

    public static ImageResp from(List<ImageItem> images) {
        return new ImageResp(images.size(), images);
    }
}
