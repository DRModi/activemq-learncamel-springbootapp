package com.learncamel.springboot.processor;

import com.learncamel.springboot.domain.Item;
import com.learncamel.springboot.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class ValidateDataProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("ValidateDataProcessor: Enter -- ");

        Item item = (Item) exchange.getIn().getBody();
        log.info("Retrieved Item in ValidateDataProcessor : "+item);


        if(ObjectUtils.isEmpty(item.getSku())){
            throw new DataException("### DataException: Sku# is null/empty for : "+item.getItemDescription());
        }




        log.info("ValidateDataProcessor: Exit -- ");

    }
}
