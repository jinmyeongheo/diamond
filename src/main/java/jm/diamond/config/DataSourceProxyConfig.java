package jm.diamond.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
class DataSourceProxyConfig {

//    // 쿼리는 QueryExecutionListener
//    public QueryExecutionListener queryExecutionListener(){
//        return new QueryExecutionListener() {
//            @Override
//            public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
//
//            }
//
//            @Override
//            public void afterQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
//
//            }
//        };
//    }
//
//    @Bean
//    public MethodExecutionListener jdbcTracing() {
//        // 모든 JDBC 메서드 호출을 로깅
//        return new TracingMethodListener();
//    }

    // 설정 호출 자체는 MethodExecutionListener
//    @Bean
//    public MethodExecutionListener onlyConnSettings() {
//        return new MethodExecutionListener() {
//            @Override
//            public void beforeMethod(MethodExecutionContext methodExecutionContext) {
//
//            }
//
//            @Override
//            public void afterMethod(MethodExecutionContext ctx) {
//                Object target = ctx.getTarget();
//                Method method = ctx.getMethod();
//                if (target instanceof Connection) {
//                    String name = method.getName();
//                    log.info("setting connection to {}" , name);
//                    log.info("setting connection to {}" , target);
//                    if (name.equals("setAutoCommit")
//                            || name.equals("setTransactionIsolation")
//                            || name.equals("setReadOnly")
//                            || name.equals("commit")
//                            || name.equals("rollback")) {
//                        // args와 실행 시간 등도 있음
//                        Object[] args = ctx.getMethodArgs();
//                        // 여기에 SLF4J 등으로 로깅
//                        // log.debug("conn#{}({}) took {}ms", name, Arrays.toString(args), ctx.getElapsedTime());
//                    }
//                }
//            }
//        };
//    }

}
