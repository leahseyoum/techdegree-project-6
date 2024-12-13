package com.teamtreehouse.techdegrees.model;

import java.util.Objects;

public class ToDo {
    private int id;
    private String name;
    private boolean isCompleted;

    public ToDo(String name, boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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
        return id == toDo.id && isCompleted == toDo.isCompleted && Objects.equals(name, toDo.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Boolean.hashCode(isCompleted);
        return result;
    }
}
