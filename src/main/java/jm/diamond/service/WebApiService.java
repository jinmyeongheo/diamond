package jm.diamond.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebApiService {

    @Async("approvalTaskExecutor")
    public void call() {

    }
}
