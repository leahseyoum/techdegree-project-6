package com.teamtreehouse.dao;

import com.teamtreehouse.techdegrees.dao.Sql2oToDoDao;
import com.teamtreehouse.techdegrees.model.ToDo;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertNotEquals;

public class Sql2oToDoDaoTest{

    private Sql2oToDoDao dao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oToDoDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingToDoSetsId() throws Exception {
        ToDo todo = new ToDo("test", false);
        int originalId = todo.getId();

        dao.addToDo(todo);

        assertNotEquals(originalId, todo.getId());
    }
}