package com.mskara.todoapp.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Integer todoId) {
        super("Item is not found with this id: " + todoId);
    }
}
