/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Nov 12, 2017
 *
 * @author Y@techburg
 */
@WebServlet(name = "BatchJobWaitServlet", urlPatterns = {"/BatchJobWaitServlet"}, asyncSupported = true)
public class BatchJobWaitServlet extends HttpServlet {

    @Resource(name = "concurrent/scheduledExecutor")
    private ManagedScheduledExecutorService searchExecutorService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final long jobId = Long.parseLong(req.getParameter("jobId"));

        // Start async
        AsyncContext asyncContext = req.startAsync(req, resp);

        // Batch job finish check logic
        final Predicate<BatchStatus> batchJobFinishChecker = (batchJobStatus) -> (batchJobStatus == BatchStatus.ABANDONED
                || batchJobStatus == BatchStatus.STOPPED
                || batchJobStatus == BatchStatus.FAILED
                || batchJobStatus == BatchStatus.COMPLETED);

        // Define job status check process
        final Callable<Boolean> jobFinishCheckProcess = () -> {
            // Check job status
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            BatchStatus batchJobStatus = jobOperator.getJobExecution(jobId).getBatchStatus();

            if (batchJobFinishChecker.test(batchJobStatus)) {
                this.showBatchJobStatus(jobId, batchJobStatus, asyncContext.getResponse());
                asyncContext.complete();
                return true;
            }
            return false;
        };

        final Runnable jobFinishCheckTask = new Runnable() {
            @Override
            public void run() {
                searchExecutorService.schedule(() -> {
                    try {
                        if (!jobFinishCheckProcess.call()) {
                            this.run();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(BatchJobWaitServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }, 10, TimeUnit.MILLISECONDS);
            }
        };
        jobFinishCheckTask.run();

        // Return immediately
    }

    /**
     * Show batch status for the given job id
     *
     * @param jobId
     * @param batchStatus
     * @param resp
     */
    private void showBatchJobStatus(long jobId, BatchStatus batchStatus, ServletResponse resp) {
        // Print out result and complete the async context
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>CSV-to-Database Chunk Job</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Job status</h1>");
            out.println("Job " + jobId + " has finished with status = " + batchStatus);
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ioe) {
            Logger.getLogger(BatchJobServlet.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
}
