//package com.ndrlslz.configuration.center.spring.test;
//
//import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
//import org.junit.Test;
//
//import java.util.concurrent.TimeUnit;
//
//import static io.restassured.RestAssured.when;
//import static org.hamcrest.CoreMatchers.is;
//
//public class BpplicationIntegrationFailoverTest extends IntegrationFailoverTestBase {
//    @Test
//    public void shouldListenPropertyOnceConnectZookeeper() throws Exception {
//        when()
//                .get("/coder")
//                .then()
//                .statusCode(200)
//                .body("address", is("TianFu Street"))
//                .body("name", is("Tom"))
//                .body("javaer", is(true))
//                .body("age", is(23))
//                .body("emailAddress", is("tom@gmail.com"))
//                .body("app", is("customer-api"));
//
//        testingServer.restart();
//
////        createConfigurationCenterClient();
////
////        initZookeeperData();
//
//        TimeUnit.SECONDS.sleep(1);
//
//        String value = configurationCenterClient.getProperty(APPLICATION, ENVIRONMENT, "name").getValue();
//        System.out.println("@@@@@@@@@@" + value);
//        when()
//                .get("/coder")
//                .then()
//                .log()
//                .all()
//                .statusCode(200)
//                .body("name", is("Nick"))
//                .body("emailAddress", is("tom@gmail.com"));
//    }
//
//    private void initZookeeperData() throws ConfigurationCenterException {
//    }
//}
