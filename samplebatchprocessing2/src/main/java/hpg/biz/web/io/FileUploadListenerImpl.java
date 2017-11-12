/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.web.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * Created on Nov 5, 2017
 *
 * @author Y@techburg
 */
@Dependent
public class FileUploadListenerImpl implements ReadListener {

    private final String fileName;
    private final String dispatchUrl;
    private final AsyncContext asyncContext;
    private final Queue uploadDataQueue;

    /**
     * Constructor
     *
     * @param fileName
     * @param dispatchUrl
     * @param asyncContext
     * @param uploadDataQueue
     */
    public FileUploadListenerImpl(String fileName, String dispatchUrl, AsyncContext asyncContext, Queue uploadDataQueue) {
        this.fileName = fileName;
        this.dispatchUrl = dispatchUrl;
        this.asyncContext = asyncContext;
        this.uploadDataQueue = uploadDataQueue;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onDataAvailable() throws IOException {
        try {
            // Start to read data from the input stream and put to the queue
            System.out.println("hpg.biz.web.io.ReadListenerImpl.onDataAvailable()");
            // Parse file content
            ServletRequest servletRequest = asyncContext.getRequest();
            Part filePart = ((HttpServletRequest) servletRequest).getPart(fileName);
            InputStream fileInputStream = filePart.getInputStream();

            // Read file content
            byte b[] = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = fileInputStream.read(b)) != -1) {
                String data = new String(b, 0, len);
                sb.append(data);
                System.out.print("Trying to read data from file: " + data);
            }

            // EXPERIMENT: Try to read all data left in the servlet input stream. This work !?
            ServletInputStream servletInputStream = servletRequest.getInputStream();
            while (servletInputStream.isReady() && (len = servletInputStream.read(b)) != -1) {
                String data = new String(b, 0, len);
                System.out.print("Trying to read remaining data from servlet input stream: " + data);
            }

            // Add upload data to queue
            this.uploadDataQueue.add(sb.toString());
        } catch (ServletException ex) {
            Logger.getLogger(FileUploadListenerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
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
