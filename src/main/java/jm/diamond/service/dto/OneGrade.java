package jm.diamond.service.dto;

import java.util.List;

/**
 * 일급컬랙션 : 단하나의 필드만 가지며 그것은 컬랙션이다.
 * 비즈니스종속
 * -> 생성시 벨리데이션 추가
 * 불변 private getter final은 재할당을 막는것
 * 상태와행위를 한곳에
 * 이름있는 컬랙션
 * -> 일급컬랙션 명을 사용하면 보다 명확하게 표현이 가능하다.
 * -> 변수명보다 좋음.
 * https://jojoldu.tistory.com/412
 * */
public class OneGrade {
   private List<String> grade;

   public OneGrade(List<String> grade) {
      // validate
      this.grade = grade;
   }


}
