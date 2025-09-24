package jm.diamond.batch;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateParameter {

    /** yyMMdd */
    private String requestDate;

    public LocalDateParameter(String requestDate) {
        if (requestDate == null) { // 스케쥴링 배치의 requestDate 기본값은 배치 실행일이다.
            requestDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        this.requestDate = requestDate;
    }

    public LocalDate getValue() {
        return LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public String getValueAsString() {
        return requestDate;
    }

    public String getyyMMddAsString(){

        return requestDate.substring(2);
    }
}
