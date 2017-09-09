/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.web.io;

import java.io.IOException;
import java.util.Queue;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * Created on Sep 9, 2017
 *
 * @author Y@techburg
 */
public class WriteListenerImpl implements WriteListener {

    private final ServletOutputStream servletOutputStream;
    private final AsyncContext asyncContext;
    private final Queue queue;

    /**
     * The constructor
     *
     * @param servletOutputStream
     * @param asyncContext
     * @param queue
     */
    public WriteListenerImpl(ServletOutputStream servletOutputStream, AsyncContext asyncContext, Queue queue) {
        this.servletOutputStream = servletOutputStream;
        this.asyncContext = asyncContext;
        this.queue = queue;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onWritePossible() throws IOException {
        // Write when there is data and is ready to write
        while (queue.peek() != null && servletOutputStream.isReady()) {
            String data = (String) queue.poll();
            servletOutputStream.print(data);
        }
        // Complete the async process when there is no data left to write
        if (queue.peek() == null) {
            asyncContext.complete();
        }
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
