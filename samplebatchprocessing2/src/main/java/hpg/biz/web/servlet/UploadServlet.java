/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.web.servlet;

import hpg.biz.web.io.FileUploadListenerImpl;
import hpg.model.FileUploadSession;
import java.io.IOException;
import java.util.Queue;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Nov 5, 2017
 *
 * @author Y@techburg
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/uploadServlet"}, asyncSupported = true)
@MultipartConfig
public class UploadServlet extends HttpServlet {

    /**
     * Name of upload file element
     */
    private static final String UPLOAD_FILE_NAME = "fileToUpload";

    /**
     * Path of batch job start servlet
     */
    private static final String BATCH_JOB_START_URL = "/BatchJobStartServlet";

    @Inject
    private FileUploadSession fileUploadSession;

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

        // Set listener for file input stream
        Queue uploadDataQueue = this.fileUploadSession.getUploadDataQueue();
        uploadDataQueue.clear();
        ServletInputStream servletInputStream = req.getInputStream();
        ReadListener inputStreamReadListener = new FileUploadListenerImpl(UPLOAD_FILE_NAME, BATCH_JOB_START_URL, asyncContext, uploadDataQueue);
        servletInputStream.setReadListener(inputStreamReadListener);

        // Return immediately
    }

}
