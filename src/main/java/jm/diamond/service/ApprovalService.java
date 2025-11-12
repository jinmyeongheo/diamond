package jm.diamond.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final EmailService emailService;
    private final ImageService imageService;
    private final WebApiService webApiService;

    public void approve() {
        int imageCnt = 4;

        for (int i = 0; i < imageCnt; i++) {
            imageService.uploadImage();
        }

        emailService.sendEmail();

        webApiService.call();
    }
}
