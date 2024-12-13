package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.ToDo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oToDoDao implements ToDoDao{
    private final Sql2o sql2o;

    public Sql2oToDoDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public void addToDo(ToDo toDo) throws DaoException{
        String sql = "INSERT INTO todos(name, is_completed) VALUES(:name, :is_completed)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .addParameter("name", toDo.getName())
                    .addParameter("is_completed", toDo.isCompleted())
                    .executeUpdate()
                    .getKey();
            toDo.setId(id);
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem adding ToDo");
        }
    }

    @Override
    public List<ToDo> findAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM todos")
                    .executeAndFetch(ToDo.class);
        }
    }

    @Override
    public ToDo findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM todos WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(ToDo.class);
        }
    }

    @Override
    public void deleteToDo(int id) {
        try(Connection con = sql2o.open()){
            con.createQuery("DELETE FROM todos WHERE id = :id")
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    @Override
    public void updateToDo(int id, String name, boolean is_completed) {

        String sql = "UPDATE todos SET name = :name, is_completed = :is_completed WHERE id = :id";

        try(Connection con = sql2o.open()){
             con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("is_completed", is_completed)
                    .executeUpdate();
        }
    }
}
