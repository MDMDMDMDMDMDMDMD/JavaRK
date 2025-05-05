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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/todos")
@Tag(name = "Public Todo", description = "Public Todo APIs that don't require authentication")
public class PublicTodoController {

    private final TodoService todoService;

    @Autowired
    public PublicTodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Get all todos (public)", description = "Retrieves a list of all todo items without authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved todos",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class)))
    })
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @Operation(summary = "Get todo by ID (public)", description = "Retrieves a specific todo by its ID without authentication")
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

    @Operation(summary = "Create a new todo (public)", description = "Creates a new todo item without authentication")
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
}