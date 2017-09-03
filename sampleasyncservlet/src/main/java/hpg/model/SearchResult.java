/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hpg.model;

import java.io.Serializable;

/**
 * Created on Aug 27, 2017
 *
 * @author Y@techburg
 */
public class SearchResult implements Serializable {

    private final String lineContent;
    private final String filename;
    private final int line;

    public SearchResult(String lineContent, String filename, int line) {
        this.lineContent = lineContent;
        this.filename = filename;
        this.line = line;
    }

    /**
     *
     * @return the lineContent
     */
    public String getLineContent() {
        return lineContent;
    }

    /**
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     *
     * @return the line
     */
    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "SearchResult: {" + "file=" + filename + ", line=" + line + ",content=" + lineContent + '}';
    }
}
