/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.job;

import hpg.model.FileUploadStorage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created on Oct 16, 2017
 *
 * @author Y@techburg
 */
@Dependent
@Named(value = "newHireItemReader")
public class NewHireItemReader extends AbstractItemReader {

    /**
     * Bufferred reader for current file data
     */
    private BufferedReader bufferedReader;

    /**
     * Application-scoped storage (queue-based) for upload file data
     */
    @Inject
    private FileUploadStorage fileUploadStorage;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        //Experiment
        BlockingQueue<Queue> uploadData = fileUploadStorage.getApplicationUploadFileQueue();
        Queue uploadDataQueue = uploadData.take();

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
