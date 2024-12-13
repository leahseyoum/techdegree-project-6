package com.teamtreehouse.techdegrees.dao;

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
    public void addToDo(ToDo toDo) {
        String sql = "INSERT INTO todos(name, isCompleted) VALUES(:name, :isCompleted)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(toDo)
                    .executeUpdate()
                    .getKey();
            toDo.setId(id);
        }catch(Sql2oException ex){

        }
    }

    @Override
    public List<ToDo> findAll() {
        return List.of();
    }

    @Override
    public ToDo findById(int id) {
        return null;
    }

    @Override
    public void deleteToDo(int id) {

    }

    @Override
    public ToDo updateToDo(int id) {
        return null;
    }
}
