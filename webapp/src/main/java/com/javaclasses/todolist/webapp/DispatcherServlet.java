package com.javaclasses.todolist.webapp;


import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Dispatcher servlet which handles all user's requests
 */
public class DispatcherServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerRegistry registry = HandlerRegistry.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (log.isInfoEnabled()) {
            log.info("Start executing user's request...");
        }

        final String uri = request.getRequestURI();
        final String method = request.getMethod().toUpperCase();
        final RequestContext requestContext = new RequestContext(uri, method);

        final Handler handler = registry.getHandler(requestContext);
        final JsonEntity jsonEntity = handler.process(request, response);
        final PrintWriter printWriter = response.getWriter();
        response.setContentType(APPLICATION_JSON);
        printWriter.write(jsonEntity.generateJson());

        if (log.isInfoEnabled()) {
            log.info("Generated response: " + jsonEntity.generateJson());
        }

        response.setStatus(jsonEntity.getResponseStatusCode());

        if (log.isInfoEnabled()) {
            log.info("Response successfully created.");
        }
    }
}
