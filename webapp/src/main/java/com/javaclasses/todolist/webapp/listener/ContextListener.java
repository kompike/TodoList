package com.javaclasses.todolist.webapp.listener;

import com.javaclasses.todolist.webapp.controller.TaskController;
import com.javaclasses.todolist.webapp.controller.UserController;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context listener for available controllers initialization
 */
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserController.init();
        TaskController.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
