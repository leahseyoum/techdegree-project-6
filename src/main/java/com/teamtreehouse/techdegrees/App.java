package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oToDoDao;
import com.teamtreehouse.techdegrees.dao.ToDoDao;
import com.teamtreehouse.techdegrees.exc.ApiError;
import com.teamtreehouse.techdegrees.model.ToDo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");

//        String datasource = "jdbc:h2:"

        Sql2o sql2o = new Sql2o("jdbc:h2:~/todos.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
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
            res.status(200);
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

        after((req, res) -> {
            res.type("application/json");
        });

    }

}
