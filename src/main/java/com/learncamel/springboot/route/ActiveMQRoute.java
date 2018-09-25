package com.learncamel.springboot.route;

import com.learncamel.springboot.domain.Item;
import com.learncamel.springboot.exception.DataException;
import com.learncamel.springboot.processor.BuildSQLProcessor;
import com.learncamel.springboot.processor.MailProcessor;
import com.learncamel.springboot.processor.SuccessProcessor;
import com.learncamel.springboot.processor.ValidateDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Slf4j
public class ActiveMQRoute extends RouteBuilder {


    @Autowired
    Environment environment;

    @Qualifier("myDataSource")
    @Autowired
    DataSource myDataSource;

    @Autowired
    BuildSQLProcessor buildSQLProcessor;

    @Autowired
    SuccessProcessor successProcessor;

    @Autowired
    MailProcessor mailProcessor;

    @Autowired
    ValidateDataProcessor validateDataProcessor;


    @Override
    public void configure() throws Exception {

        //no need to create LoggerFactory b'coz we are using @Sl4j lombok (see additional info)
        log.info("Before Route Starting - CAMEL ROUTE");


        //GSON Data Format Defined
        GsonDataFormat itemDataFormat = new GsonDataFormat(Item.class);

        //this will be raised in DB connectivity issue - so retry make sense
        onException(PSQLException.class).log(LoggingLevel.ERROR, "PSQL Exception in the route and Body is ${body}")
                .maximumRedeliveries(3).redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR);

        //Basically no need of retry since it is data issue and in this case it is going to be same exception in all retry.
        onException(DataException.class).log(LoggingLevel.ERROR, "DataException in Route and Body is ${body}")
                .process(mailProcessor)
                .to("{{toErrorQueue}}");


        //Using predicates and content based routing - achieving the mock testing
        from("{{fromRoute}}").routeId("mainRouteId")
                    .log("From Route : Environment is " + environment.getProperty("message") +"\n"+" Read Message From Queue : ${body}")
                .unmarshal(itemDataFormat)
                    .log("Converted Object : ${body}")
                .process(validateDataProcessor)
                .process(buildSQLProcessor)
                .to("{{tojdbcRoute}}")
                .to("{{toSQLNode}}") // here we are doing select but similar way we can do for update and delete, cont...
                                    // using choice() and by setting operation also part of the header from build processor
                .log("Retrieved the inserted record from DB -  ${body}");


        log.info("After Route Completing - CAMEL ROUTE");
    }
}
