package jm.diamond.component_service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Component Service
 *
 * DAO를 의존해서는 안 된다.
 * 하나의 작업은 Transaction을 지켜야 한다.
 * 추상화 수준을 지키기 위해 비지니스 로직을 처리해선 안 된다. (이 내용은 필자가 언급하지 않았지만, Component Service는 하나의 명세서처럼 읽혀야 한다고 생각한다.)
 *
 *
 * Module Service
 *
 * DAO를 의존한다.
 * SRP를 준수 해야한다.
 * https://jaeseo0519.tistory.com/314
 * https://jangjjolkit.tistory.com/62
 **/
@Service
@Transactional
public class ComponentServiceImpl implements ComponentService {

}
