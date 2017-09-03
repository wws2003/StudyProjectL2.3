/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Aug 27, 2017
 *
 * @author Y@techburg
 */
@WebServlet("/RandomNumber")
public class RandomNumberServlet extends HttpServlet {

    @Resource(name = "concurrent/scheduledExecutor")
    private ManagedScheduledExecutorService executorService;
    private final AtomicInteger number;

    /**
     * Constructor
     *
     */
    public RandomNumberServlet() {
        this.number = new AtomicInteger();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        executorService.scheduleAtFixedRate(() -> {
            number.set((int) (Math.random() * 100));
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        // Simply write to response
        PrintWriter out = resp.getWriter();
        out.write("<html>");
        out.write("<head>");
        out.write("</head>");
        out.write("<body>");
        out.write("<h1>Number: </h1>");
        out.write(number.toString());
        out.write("</body>");
        out.write("</html>");
    }

}
