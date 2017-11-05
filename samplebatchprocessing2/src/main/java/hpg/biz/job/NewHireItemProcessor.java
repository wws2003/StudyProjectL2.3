/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.job;

import hpg.model.entity.NewHire;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Created on Oct 16, 2017
 *
 * @author Y@techburg
 */
@ApplicationScoped
@Named(value = "newHireItemProcessor")
public class NewHireItemProcessor implements ItemProcessor {

    private final SimpleDateFormat format = new SimpleDateFormat("M/dd/yy");

    @Override
    public Object processItem(Object t) throws Exception {
        // Parse item read by NewHireItemReader
        System.out.println("processItem: " + t);

        StringTokenizer tokens = new StringTokenizer((String) t, ",");

        String name = tokens.nextToken();
        String dateStr;

        try {
            dateStr = tokens.nextToken();
            format.setLenient(false);
            format.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
        Date currentDate = new Date();
        return new NewHire(name, dateStr, currentDate, currentDate);
    }
}
