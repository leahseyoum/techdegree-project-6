package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oToDoDao;
import com.teamtreehouse.techdegrees.dao.ToDoDao;
import com.teamtreehouse.techdegrees.model.ToDo;
import com.teamtreehouse.testing.ApiClient;
import com.teamtreehouse.testing.ApiResponse;
//import jdk.internal.classfile.components.ClassPrinter;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

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
    public void todosCanBeAccessedById() throws Exception{
        ToDo todo = new ToDo("test", false);
        todoDao.addToDo(todo);
        int id = todo.getId();

        ApiResponse res = client.request("GET", "/api/v1/todos" + todo.getId());
        ToDo retrieved = gson.fromJson(res.getBody(), ToDo.class);

        assertEquals(todo, retrieved);
    }
}