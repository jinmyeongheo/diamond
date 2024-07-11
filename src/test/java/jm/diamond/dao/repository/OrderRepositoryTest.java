package jm.diamond.dao.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import jm.diamond.dao.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

/** why DataJpaTest
 * 1. 기본 설정 포함: 데이터베이스에 관련된 기본 설정(@EntityScan, @EnableJpaRepositories, @Transactional 등)을 자동으로 설정해줍니다.
 * 2. 내장형 데이터베이스 사용: 내장형 데이터베이스(H2, HSQL, Derby 등)를 기본적으로 사용하여 테스트 환경을 구성합니다. 실제 데이터베이스에 영향을 주지 않으며 빠른 테스트가 가능합니다.
 * 3. 트랜잭션 관리: 테스트가 끝나면 트랜잭션을 롤백하여 데이터베이스 상태를 원래대로 유지합니다.
 * 4. 필요한 Bean만 로드: Repository, EntityManager와 같은 JPA 관련 빈만 로드하여 테스트 속도를 높입니다.
 * */
@DataJpaTest
//@SpringBootTest
// https://sormuras.github.io/blog/2018-09-13-junit-4-core-vs-jupiter-api.html
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

   /**
    * JUnit 4를 사용하는 경우, @RunWith(SpringRunner.class)를 테스트에 추가하는 것을 잊지 마세요.
    * 그렇지 않으면 어노테이션이 무시됩니다.
    * JUnit 5를 사용하는 경우, @SpringBootTest 및 다른 @…Test 어노테이션이 이미 해당 어노테이션으로 주석이 달려 있기 때문에
    * @ExtendWith(SpringExtension.class)를 추가할 필요가 없습니다.*/
   // https://sunghs.tistory.com/138
   // https://www.inflearn.com/questions/720727/junit-test-di-%EB%B0%A9%EC%8B%9D-%EC%A7%88%EB%AC%B8%EB%93%9C%EB%A6%BD%EB%8B%88%EB%8B%A4

   @Autowired
   private OrderRepository orderRepository;

   @Test
   void test(){
      Optional<Order> byId =
          orderRepository.findById("2102021");

      if(byId.isPresent()){
         System.out.println("byId = " + byId.get());
      }else{
         System.out.println("byId = " + byId.isPresent());
      }

   }

}