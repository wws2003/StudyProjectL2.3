/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.web.io;

import java.io.IOException;
import java.util.Queue;
import javax.enterprise.context.Dependent;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * Created on Nov 5, 2017
 *
 * @author Y@techburg
 */
@Dependent
public class FileUploadListenerImpl implements ReadListener {

    private final ServletInputStream servletInputStream;
    private final String dispatchUrl;
    private final AsyncContext asyncContext;
    private final Queue uploadDataQueue;

    /**
     * Constructor
     *
     * @param servletInputStream
     * @param dispatchUrl
     * @param asyncContext
     * @param uploadDataQueue
     */
    public FileUploadListenerImpl(ServletInputStream servletInputStream, String dispatchUrl, AsyncContext asyncContext, Queue uploadDataQueue) {
        this.servletInputStream = servletInputStream;
        this.dispatchUrl = dispatchUrl;
        this.asyncContext = asyncContext;
        this.uploadDataQueue = uploadDataQueue;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onDataAvailable() throws IOException {
        // Start to read data from the input stream and put to the queue
        System.out.println("hpg.biz.web.io.ReadListenerImpl.onDataAvailable()");
        StringBuilder sb = new StringBuilder();
        int len = -1;
        byte b[] = new byte[1024];
        while (servletInputStream.isReady() && (len = servletInputStream.read(b)) != -1) {
            String data = new String(b, 0, len);
            sb.append(data);
        }

        this.uploadDataQueue.add(sb.toString());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onAllDataRead() throws IOException {
        // Forward to job start page
        this.asyncContext.dispatch(this.dispatchUrl);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onError(Throwable t) {
        this.asyncContext.complete();
        t.printStackTrace();
    }

}
