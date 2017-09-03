/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.biz;

import hpg.model.SearchResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

/**
 * Created on Aug 27, 2017
 *
 * @author Y@techburg
 */
@Stateless
public class MultiSearchEJB {

    @Resource(name = "concurrent/executor")
    private ManagedExecutorService executorService;

    /**
     * Do search job against search parameter
     *
     * @param search
     * @return
     */
    public List<SearchResult> search(final String search) {
        List<SearchResult> results = Collections.synchronizedList(new ArrayList<SearchResult>());
        Semaphore semaphore = new Semaphore(0);
        List<File> files = new ArrayList<>();
        files.add(new File("/home/pham/projects/StudyProjectsL3.2/sampleasyncservlet/testdocs/inject_vs_ejb.txt"));
        files.add(new File("/home/pham/projects/StudyProjectsL3.2/sampleasyncservlet/testdocs/jsf.txt"));
        files.add(new File("/home/pham/projects/StudyProjectsL3.2/sampleasyncservlet/testdocs/managed_scheduled_executor_service.txt"));
        files.add(new File("/home/pham/projects/StudyProjectsL3.2/sampleasyncservlet/testdocs/schedule_executor_service.txt"));
        files.stream().forEach((file) -> {
            executorService.execute(new SearchTask(file, search, semaphore, results));
        });
        try {
            // Try to wait 40ms until all the permits are released (i.e. all file searches are done)
            semaphore.tryAcquire(files.size(), 40, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiSearchEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    // Inner class for search task
    class SearchTask implements Runnable {

        private final File file;
        private final String searchString;
        private final Semaphore semaphore;
        private final List<SearchResult> allResults;

        /**
         * Constructor for a search task against a file
         *
         * @param file
         * @param searchString
         * @param semaphore
         * @param allResults
         */
        private SearchTask(File file, String searchString, Semaphore semaphore, List<SearchResult> allResults) {
            this.file = file;
            this.searchString = searchString;
            this.semaphore = semaphore;
            this.allResults = allResults;
        }

        @Override
        public void run() {
            doSearch();
            semaphore.release();
        }

        /**
         * Conduct the search
         */
        private void doSearch() {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                int count = 0;
                String filename = file.getAbsolutePath();
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    if (count <= 3 && line.contains(searchString)) {
                        allResults.add(new SearchResult(line, filename, lineNumber));
                        count++;
                    }
                    lineNumber++;
                }
            } catch (IOException ex) {
                Logger.getLogger(MultiSearchEJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
