package com.martynatyran.domain.cat.controller;

import com.martynatyran.domain.cat.bo.ICatBO;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/cats")
@AllArgsConstructor
public class CatController {

    private final ICatBO catBO;

    @ApiOperation(
            value = "Get all cats",
            notes = "Finds all saved cats, returns them as list"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Found list of all cats")
    })
    @GetMapping()
    public ResponseEntity<List<CatSnapshot>> findCats() {
        List<CatSnapshot> cats = catBO.findAll();
        return ResponseEntity.ok(cats);
    }

    @ApiOperation(
        value = "Add a cat",
        notes = "Saves a cat, returns an URI to the created cat according to the pattern" +
                "/cats/(id)"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 201,
            message = "A cat was created")
    })
    @PostMapping()
    public HttpEntity<Void> add(@Valid @RequestBody CatNew catNew) {
        Long id = catBO.add(catNew);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(
        value = "Delete a cat",
        notes = "Deletes a cat"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 204,
            message = "A cat was deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        catBO.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
        value = "Edit a cat",
        notes = "Edits a cat"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 204,
            message = "A cat was edited"),
        @ApiResponse(code = 400,
            message = "Bad request"),
        @ApiResponse(code = 404,
            message = "Cat not found")
    })
    @PutMapping("/{id}")
    public HttpEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody CatNew catNew) {
        catBO.edit(id, catNew);
        return ResponseEntity.noContent().build();
    }
}
