package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.ToDo;

import java.util.List;

public interface ToDoDao {
    List<ToDo> findAll();
    ToDo findById(int id);
    void deleteToDo(int id);
    ToDo updateToDo(int id);
    // edit add method to throw Dao exception
    void addToDo(ToDo toDo) throws DaoException;
}
