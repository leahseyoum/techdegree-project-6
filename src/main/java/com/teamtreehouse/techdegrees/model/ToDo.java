package com.teamtreehouse.techdegrees.model;

import java.util.Objects;

public class ToDo {
    private int id;
    private String name;
    private boolean is_completed;

    public ToDo(String name, boolean is_completed) {
        this.name = name;
        this.is_completed = is_completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return is_completed;
    }

    public void setCompleted(boolean completed) {
        is_completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDo toDo = (ToDo) o;
        return id == toDo.id && is_completed == toDo.is_completed && name.equals(toDo.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + Boolean.hashCode(is_completed);
        return result;
    }
}
