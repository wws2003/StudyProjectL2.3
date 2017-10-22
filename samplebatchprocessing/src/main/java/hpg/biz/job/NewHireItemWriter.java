/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.job;

import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created on Oct 16, 2017
 *
 * @author Y@techburg
 */
@ApplicationScoped
@Named(value = "newHireItemWriter")
public class NewHireItemWriter extends AbstractItemWriter {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void writeItems(List<Object> items) throws Exception {
        // List of items, each is the output of processor
        items.stream().forEach(item -> em.persist(item));
        // No need to commit, flush...?
    }
}
