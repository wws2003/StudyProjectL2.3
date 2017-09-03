/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.web.servlet;

import hpg.biz.MultiSearchEJB;
import hpg.model.SearchResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
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
@WebServlet("/MultiSearchServlet")
public class MultiSearchServlet extends HttpServlet {

    @Inject
    private MultiSearchEJB ejb;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet MultiSearchServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Search Results:</h1>");
        out.println("<ul>");
        List<SearchResult> searchResults = ejb.search(request.getParameter("search"));
        searchResults.stream().forEach((result) -> {
            out.println("<li>");
            out.println(result);
            out.println("</li>");
        });
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }
}
