package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oToDoDao;
import com.teamtreehouse.techdegrees.model.ToDo;
import com.teamtreehouse.testing.ApiClient;
import com.teamtreehouse.testing.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AppTest{

    public static final String PORT = "4568";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oToDoDao todoDao;

    @BeforeClass
    public static void startServer() throws Exception {
        String[] args = {PORT, TEST_DATASOURCE};
        App.main(args);
    }

    @AfterClass
    public static void stopServer() throws Exception {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
       Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
       conn = sql2o.open();
       client = new ApiClient("http://localhost:" + PORT);
       gson = new Gson();
       todoDao = new Sql2oToDoDao(sql2o);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingToDosReturnsCreatedStatus() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test");
        values.put("is_completed", false);

        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));

        assertEquals(201, res.getStatusCode());
    }

    @Test
    public void todoCanBeCreatedSuccessfully() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test");
        values.put("is_completed", false);

        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));

        ToDo created = gson.fromJson(res.getBody(), ToDo.class);

        assertNotNull(created.getId());
        assertEquals("Test", created.getName());
        assertFalse(created.isCompleted());
    }

    @Test
    public void multipleToDosReturnedWithFindAll() throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);
        ToDo todo2 = new ToDo("test2", false);
        todoDao.addToDo(todo2);
        ToDo todo3 = new ToDo("test3", false);
        todoDao.addToDo(todo3);

        ApiResponse res = client.request("GET", "/api/v1/todos");
        ToDo[] todosArr = gson.fromJson(res.getBody(), ToDo[].class);

        assertEquals(3, todosArr.length);
    }

    @Test
    public void todosCanBeAccessedById() throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);

        ApiResponse res = client.request("GET", "/api/v1/todos/" + todo.getId());
        ToDo retrieved = gson.fromJson(res.getBody(), ToDo.class);

        assertEquals(todo, retrieved);
    }

    @Test
    public void todoCanBeSuccessfullyDeleted () throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);
        int id = todo.getId();

        ApiResponse res = client.request("DELETE", "/api/v1/todos/" + todo.getId());

        assertEquals(204, res.getStatusCode());
        assertNull(todoDao.findById(id));
    }

    @Test
    public void deletingToDoReturnsNoContentStatus() throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);
        int id = todo.getId();

        ApiResponse res = client.request("DELETE", "/api/v1/todos/" + todo.getId());

        assertEquals(204, res.getStatusCode());
    }

    @Test
    public void todoCanBySuccessfullyUpdated() throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);
        Map<String, Object> values = new HashMap<>();
        values.put("name", "change");
        values.put("is_completed", true);

        ApiResponse res = client.request("PUT", "/api/v1/todos/" + todo.getId(), gson.toJson(values));
        ToDo updated = gson.fromJson(res.getBody(), ToDo.class);

        assertEquals("change", updated.getName());
        assertTrue(updated.isCompleted());
    }

    @Test
    public void updatingTodoReturnsSuccessfulStatus() throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);
        Map<String, Object> values = new HashMap<>();
        values.put("name", "change");
        values.put("is_completed", true);

        ApiResponse res = client.request("PUT", "/api/v1/todos/" + todo.getId(), gson.toJson(values));

        assertEquals(200, res.getStatusCode());
    }

    @Test
    public void missingToDoReturns404NotFoundStatus() throws Exception{
       ApiResponse res = client.request("GET", "/api/v1/todos/50");

       assertEquals(404, res.getStatusCode());
    }
}