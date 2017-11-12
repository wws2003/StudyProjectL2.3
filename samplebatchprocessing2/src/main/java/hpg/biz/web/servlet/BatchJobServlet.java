/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.web.servlet;

import hpg.model.FileUploadSession;
import hpg.model.FileUploadStorage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchRuntime;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Nov 5, 2017
 *
 * @author Y@techburg
 */
@WebServlet(name = "BatchServlet", urlPatterns = {"/BatchServlet"})
public class BatchJobServlet extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    /**
     * Uploaded data in session
     */
    @Inject
    private FileUploadSession fileUploadSession;

    /**
     * Application-scoped uploaded data
     */
    @Inject
    private FileUploadStorage fileUploadStorage;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        moveUploadFileFromSessionToStorage();
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        moveUploadFileFromSessionToStorage();
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>CSV-to-Database Chunk Job</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>CSV-to-Database Chunk Job</h1>");

            // Get job operator and submit the job specified by id
            JobOperator jo = BatchRuntime.getJobOperator();
            long jid = jo.start("newHireJob", new Properties());

            out.println("Job submitted: " + jid + "<br>");
            out.println("Job status after submitted: "
                    + jo.getJobExecution(jid).getBatchStatus().name()
                    + "<br>");
            out.println("<br><br>Check server.log for output, also look at \"newHireJob.xml\" for Job XML.");
            out.println("</body>");
            out.println("</html>");
        } catch (JobStartException | JobSecurityException ex) {
            Logger.getLogger(BatchJobServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Move uploaded file data from session to application-scoped storage <br>
     * in order to access from job item reader
     */
    private void moveUploadFileFromSessionToStorage() {
        Queue uploadDataQueue = fileUploadSession.getUploadDataQueue();
        Queue copiedUploadData = new LinkedBlockingDeque(uploadDataQueue);
        uploadDataQueue.clear();
        BlockingQueue<Queue> uploadFileQueue = fileUploadStorage.getApplicationUploadFileQueue();
        try {
            // Put upload file data queue into application-scoped data queue
            uploadFileQueue.put(copiedUploadData);
        } catch (InterruptedException ex) {
            Logger.getLogger(BatchJobServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
