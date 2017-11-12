/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.model;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.SessionScoped;

/**
 * Created on Nov 5, 2017
 *
 * @author Y@techburg
 */
@SessionScoped
public class FileUploadSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Queue uploadDataQueue = new LinkedBlockingQueue();

    /**
     * Get the upload data queue
     *
     * @return
     */
    public Queue getUploadDataQueue() {
        return uploadDataQueue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FileUploadSession{" + "uploadDataQueue=" + uploadDataQueue + '}';
    }
}
