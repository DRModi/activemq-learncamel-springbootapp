package com.learncamel.springboot.processor;

import com.learncamel.springboot.domain.Item;
import com.learncamel.springboot.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class BuildSQLProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("Enter into the BuildSQL Processor Class");

        if(exchange.getIn().getBody()!=null)
        {

            log.info("Exchange body is not null");




            StringBuilder sqlQuery = new StringBuilder();
            Item item = exchange.getIn().getBody(Item.class);
            String tableName = "ITEMS";


//            //it is springframework object util class to validate null/empty check
//            if(ObjectUtils.isEmpty(item.getSku())){
//                throw new DataException(" @@@@@@@@ SKU# is NULL or EMPTY for "+item.getSku());
//            }


            if("add".equalsIgnoreCase(item.getTransactionType())){

                log.info("Add Product");

                sqlQuery.append("INSERT INTO "+ tableName +" (SKU, ITEM_DESCRIPTION, PRICE) VALUES "+"('");
                sqlQuery.append(item.getSku()+"','"+item.getItemDescription()+"',"+item.getItemPrice()+")");

            }else if("update".equalsIgnoreCase(item.getTransactionType())){

                log.info("Update Product");

                sqlQuery.append("UPDATE "+ tableName +" SET PRICE=" + item.getItemPrice());
                sqlQuery.append(" where SKU = '"+item.getSku()+"'");


            }else if("delete".equalsIgnoreCase(item.getTransactionType())){

                log.info("Delete Product");

                sqlQuery.append("DELETE FROM "+ tableName +" where SKU = '"+item.getSku()+"'");

            }

            log.info("** Constructed Query is : "+sqlQuery);

            exchange.getIn().setBody(sqlQuery);
            exchange.getIn().setHeader("skuId", item.getSku());
        }

    }
}
