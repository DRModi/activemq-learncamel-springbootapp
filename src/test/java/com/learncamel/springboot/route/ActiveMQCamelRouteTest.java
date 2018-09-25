package com.learncamel.springboot.route;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActiveMQCamelRouteTest extends CamelTestSupport {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    protected CamelContext createCamelContext(){
        return  camelContext;
    }

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ConsumerTemplate consumerTemplate;

    @Autowired
    Environment environment;

    @Before
    public void setUp() {

    }

    @Test
    public void ActiveMQRouteTest(){

        String inputJSONRecord = "{\"transactionType\":\"add\", \"sku\":\"511\", \"itemDescription\":\"Apple iMac\", \"itemPrice\":\"2750\"}";

        ArrayList responseList = (ArrayList) producerTemplate.requestBody("activemq:inputItemQueue", inputJSONRecord);

        System.out.println("Response List : "+ responseList);

        assertNotNull(responseList);
    }

    @Test(expected = CamelExecutionException.class)
    public void ActiveMQRouteTest_Exception(){

        String inputJSONRecord = "{\"transactionType\":\"add\", \"sku\":\"\", \"itemDescription\":\"Apple iMac\", \"itemPrice\":\"2750\"}";

        ArrayList responseList = (ArrayList) producerTemplate.requestBody("activemq:inputItemQueue", inputJSONRecord);

//        System.out.println("Response List : "+ responseList);
//
//        assertNotNull(responseList);
    }

}
