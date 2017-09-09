/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package hpg.web.servlet;

import hpg.web.io.ReadListenerImpl;
import hpg.web.io.WriteListenerImpl;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Sep 9, 2017
 *
 * @author Y@techburg
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/uploadServlet"}, asyncSupported = true)
public class UploadServlet extends HttpServlet {

    /**
     * @inheritDoc
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Start async process with listener (in this case, just for debug purpose ?)
        AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.addListener(new AsyncListener() {
            /**
             * @inheritDoc
             */
            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                System.out.println("Async event started");
            }

            /**
             * @inheritDoc
             */
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("my asyncListener.onComplete");
            }

            /**
             * @inheritDoc
             */
            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                System.out.println("my asyncListener.onTimeout");
            }

            /**
             * @inheritDoc
             */
            @Override
            public void onError(AsyncEvent event) throws IOException {
                System.out.println(event.getThrowable());
            }
        },
                req,
                resp);

        // Create writer, reader instances
        Queue dataQueue = new LinkedBlockingQueue();
        WriteListener writeListener = new WriteListenerImpl(resp.getOutputStream(),
                asyncContext,
                dataQueue);
        ServletInputStream servletInputStream = req.getInputStream();
        ReadListener readListener = new ReadListenerImpl(servletInputStream,
                resp,
                asyncContext,
                writeListener,
                dataQueue);

        // Set read listener to servlet input stream then return immediately without waiting for the IO completed
        servletInputStream.setReadListener(readListener);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
}
