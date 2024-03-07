package com.example.jwave;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.util.*;

@WebServlet(name = "mainServlet", value = "/main-servlet")
@MultipartConfig
@SuppressWarnings("rawtypes")
public class MainServlet extends HttpServlet {
    static HashMap<String, Class> classes;
    static final Object processLock = new Object();
    protected static final Logger logger = LogManager.getLogger();

    public void init() {
        classes = new HashMap<>();
    }

    public MainServlet() {
        //Do nothing
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"output.zip\"");
        Part filePart = request.getPart("file");
        FileManager fileManager = new FileManager(getServletContext().getRealPath(System.getProperty("file.separator")));
        String zipName = filePart.getSubmittedFileName();
        if(zipName.length() > 4 && zipName.substring(zipName.length() - 4).compareTo(".zip") != 0) {
            return;
        }
        Part pmdPart = request.getPart("pmd");
        Part findsecuritybugsPart = request.getPart("findsecuritybugs");
        Part semgrepPart = request.getPart("semgrep");
        Part yascaPart = request.getPart("yasca");
        Part sonarqubePart = request.getPart("sonarqube");
        fileManager.zipToTemp(filePart);
        if(pmdPart != null) {
            new PMDScanner(fileManager);
        }
        if(findsecuritybugsPart != null) {
            new FindsecuritybugsScanner(fileManager);
        }
        if(sonarqubePart != null) {
            new SonarqubeScanner(fileManager);
        }
        if(semgrepPart != null) {
            new SemgrepScanner(fileManager);
        }
        if(yascaPart != null) {
            new YascaScanner(fileManager);
        }
        fileManager.outputZip(response);
    }


}
