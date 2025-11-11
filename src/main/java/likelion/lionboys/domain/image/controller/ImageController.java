package likelion.lionboys.domain.image.controller;

import likelion.lionboys.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/images")
@RestController
public class ImageController {

    ApiResponse<> upload() {
        return null;
    }
}
