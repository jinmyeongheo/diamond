package jm.diamond.config;

public class TestConfig {

    // 실제 환경에서 반복 테스트하고 처리 속도 vs 메모리 활용을 비교하여 최적 지점을 찾으라고 권장
    // JVM 메모리 제한, CPU 코어 수, 스레드 풀 크기, 메모리 부담, 트랜잭션 수
    //처리량 (throughput), 메모리 사용량, JVM 부담 (특히 partitioning 및 async 처리 시)
    // chunk size는 job마다 다르게 설정해야 한다

    // 트랜잭션: 초당 수천 건 이상 커밋이면 성능 저하
    //
    // JVM Heap: GC 논문과 JVM 벤더 가이드에서 “Old Gen 70~80% 이상 장시간 점유 → Full GC 잦아짐
    //
    // CPU: OS 차원에서 80~90% 이상 지속 점유 시 context switch와 커널 대기열이 급격히 늘어남.
    //
    // 스레드 풀: CPU 코어 × 2~4 정도
    //
     // i/o 횟수
    // GC time > 100ms → 사용자가 체감할 수 있는 지연


    //로컬 Docker: chunk/fetchSize/batchSize 변화 실험 → GC, 커밋 오버헤드, 처리량 곡선 패턴 확인.
    //
    //스테이징/제한된 클라우드 VM: 동일 chunk로 실행 → TPS 절대값, DB I/O 병목 확인.
    //
    //운영 예측: USL(Universal Scalability Law) 같은 모델로 스테이징 수치를 운영 코어/메모리 스펙에 맞게 확대.


    //    Micrometer → “잡/스텝/청크 처리량, commit 시간”을 계측
    //
    //  Prometheus → 그 수치를 초 단위로 기록
    //
    //Grafana → 성능 추세를 시각적으로 확인
}
