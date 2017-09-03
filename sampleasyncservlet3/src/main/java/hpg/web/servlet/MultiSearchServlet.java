/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.web.servlet;

import hpg.biz.MultiSearchEJB;
import hpg.model.SearchResult;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
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
@WebServlet(name = "multiSearchServlet", urlPatterns = {"/multiSearchServlet"}, asyncSupported = true)
public class MultiSearchServlet extends HttpServlet {

    @Inject
    private MultiSearchEJB ejb;

    @Resource(name = "concurrent/scheduledExecutor")
    private ManagedScheduledExecutorService searchExecutorService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Create context for an async search
        AsyncContext asyncContext = request.startAsync(request, response);

        searchExecutorService.schedule(() -> {
            // Conduct search
            List<SearchResult> searchResults = ejb.search(request.getParameter("search"));
            request.setAttribute("searchResults", searchResults);

            // Dispatch the created async context to SearchResultServlet
            asyncContext.dispatch("/searchResult.jsp");
        }, 10, TimeUnit.MILLISECONDS);

        // Return immediately
    }
}
