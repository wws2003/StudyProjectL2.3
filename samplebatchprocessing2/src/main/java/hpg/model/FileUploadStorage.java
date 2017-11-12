/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.model;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.ApplicationScoped;

/**
 * Created on Nov 12, 2017
 *
 * @author Y@techburg
 */
@ApplicationScoped
public class FileUploadStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    private final BlockingQueue<Queue> applicationUploadFileQueue = new LinkedBlockingQueue();

    /**
     * Get the application-scoped queue for all session uploaded files
     *
     * @return the applicationUploadFileQueue
     */
    public BlockingQueue<Queue> getApplicationUploadFileQueue() {
        return applicationUploadFileQueue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FileUploadStorage{" + "applicationUploadFileQueue=" + applicationUploadFileQueue + '}';
    }
}
