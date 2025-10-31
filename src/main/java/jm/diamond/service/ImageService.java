package jm.diamond.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Async("approvalTaskExecutor")
    public void uploadImage() {
        CompletableFuture.completedFuture("task end : ");
    }
}
