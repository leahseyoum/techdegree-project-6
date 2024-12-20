package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oToDoDao;
import com.teamtreehouse.techdegrees.dao.ToDoDao;
import com.teamtreehouse.techdegrees.exc.ApiError;
import com.teamtreehouse.techdegrees.model.ToDo;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");

        String datasource = "jdbc:h2:~/todos.db";
        if(args.length > 0) {
            if(args.length != 2) {
                System.out.println("java Api <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }
        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource), "", "");
        ToDoDao todoDao = new Sql2oToDoDao(sql2o);
        Gson gson = new Gson();


        post("/api/v1/todos", "application/json", (req, res) -> {
            ToDo todo = gson.fromJson(req.body(), ToDo.class);
            todoDao.addToDo(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        get("/api/v1/todos", "application/json", (req, res) ->
            todoDao.findAll(), gson::toJson);

        get("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            ToDo foundTodo = todoDao.findById(id);
            if(foundTodo == null) {
                throw new ApiError(404, "Could not find todo");
            }
            return foundTodo;
        }, gson::toJson);

        delete("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            todoDao.deleteToDo(id);
            res.status(204);
            return "";
        });

        put("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            ToDo todo = todoDao.findById(id);
            ToDo newToDo = gson.fromJson(req.body(), ToDo.class);

            String name = newToDo.getName() != null ? newToDo.getName() : todo.getName();
            boolean isCompleted = newToDo.isCompleted();

            todoDao.updateToDo(id, name, isCompleted);
            res.status(200);
            return todoDao.findById(id);
        }, gson::toJson);

        exception(ApiError.class, (exc, req, res) -> {
            ApiError err = (ApiError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        after((req, res) -> {
            res.type("application/json");
        });

    }

}
