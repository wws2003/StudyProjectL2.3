/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.job;

import hpg.model.FileUploadSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created on Oct 16, 2017
 *
 * @author Y@techburg
 */
@ApplicationScoped
@Named(value = "newHireItemReader")
public class NewHireItemReader extends AbstractItemReader {

    private BufferedReader bufferedReader;

    // Is this possible to access session scope from application scope ?
    @Inject
    private FileUploadSession fileUploadSession;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        Queue uploadDataQueue = fileUploadSession.getUploadDataQueue();
        StringBuilder sb = new StringBuilder();
        while (uploadDataQueue.peek() != null) {
            String data = (String) uploadDataQueue.poll();
            sb.append(data);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sb.toString().getBytes());

        // Initialize read from CSV file
        bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    @Override
    public Object readItem() throws Exception {
        // Read content of one line
        try {
            return bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(NewHireItemReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
