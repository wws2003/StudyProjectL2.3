/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.web.io;

import java.io.IOException;
import java.util.Queue;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Sep 9, 2017
 *
 * @author Y@techburg
 */
public class ReadListenerImpl implements ReadListener {

    private final ServletInputStream servletInputStream;
    private final HttpServletResponse httpServletResponse;
    private final AsyncContext asyncContext;
    private final WriteListener writeListener;
    private final Queue queue;

    /**
     * Constructor
     *
     * @param servletInputStream
     * @param httpServletResponse
     * @param asyncContext
     * @param writeListener
     * @param queue
     */
    public ReadListenerImpl(ServletInputStream servletInputStream, HttpServletResponse httpServletResponse, AsyncContext asyncContext, WriteListener writeListener, Queue queue) {
        this.servletInputStream = servletInputStream;
        this.httpServletResponse = httpServletResponse;
        this.asyncContext = asyncContext;
        this.queue = queue;
        this.writeListener = writeListener;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onDataAvailable() throws IOException {
        // Start to read data from the input stream and put to the queue
        System.out.println("hpg.web.io.ReadListenerImpl.onDataAvailable()");
        StringBuilder sb = new StringBuilder();
        int len = -1;
        byte b[] = new byte[1024];
        while (servletInputStream.isReady() && (len = servletInputStream.read(b)) != -1) {
            String data = new String(b, 0, len);
            sb.append(data);
        }
        queue.add(sb.toString());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onAllDataRead() throws IOException {
        // Called after all data read -> Set up a writer to write the response
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.setWriteListener(writeListener);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onError(Throwable t) {
        asyncContext.complete();
        t.printStackTrace();
    }
}
