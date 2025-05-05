package com.example.todoj.controller;

import com.example.todoj.model.Todo;
import com.example.todoj.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todo", description = "Todo management APIs")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Get all todos", description = "Retrieves a list of all todo items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved todos",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class)))
    })
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @Operation(summary = "Get todo by ID", description = "Retrieves a specific todo by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved todo",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@Parameter(description = "ID of the todo to retrieve") @PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new todo", description = "Creates a new todo item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Todo successfully created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @Operation(summary = "Update a todo", description = "Updates an existing todo by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todo successfully updated",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))),
        @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @Parameter(description = "ID of the todo to update") @PathVariable Long id, 
            @Valid @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a todo", description = "Deletes a todo by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Todo successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Todo not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@Parameter(description = "ID of the todo to delete") @PathVariable Long id) {
        if (todoService.deleteTodo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}