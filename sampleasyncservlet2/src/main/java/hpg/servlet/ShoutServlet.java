/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pham
 */
@WebServlet(name = "ShoutServlet", urlPatterns = {"/shoutServlet"}, asyncSupported = true)
public class ShoutServlet extends HttpServlet {

    private static final String MESSAGES_ATTR_NAME = "messages";
    private final List<AsyncContext> inProgressContext = new ArrayList();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Create async context with 10minute timeout
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(10 * 60 * 1000);
        // Add to waiting list, then return immediately
        inProgressContext.add(asyncContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Retrieve all current in-progress async contexts
        List<AsyncContext> asyncContexts = new ArrayList<>(this.inProgressContext);
        this.inProgressContext.clear();

        String name = req.getParameter("name");
        String message = req.getParameter("message");
        String htmlMessage = "<p><b>" + name + "</b><br/>" + message + "</p>";

        // Set message attribute into servlet context
        ServletContext servletContext = req.getServletContext();
        String messageAttribute = Optional.ofNullable(servletContext.getAttribute(MESSAGES_ATTR_NAME))
                .map(currentMsgAttr -> htmlMessage + (String) currentMsgAttr)
                .orElse(htmlMessage);
        servletContext.setAttribute(MESSAGES_ATTR_NAME, messageAttribute);

        // Write response to all async contexts
        asyncContexts.stream().forEach((asyncContext) -> {
            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                writer.println(htmlMessage);
                writer.flush();
                asyncContext.complete();
            } catch (Exception ex) {
            }
        });

        // Do not not to redirect as responses have been written already
        //resp.sendRedirect("index.jsp");
    }
}
