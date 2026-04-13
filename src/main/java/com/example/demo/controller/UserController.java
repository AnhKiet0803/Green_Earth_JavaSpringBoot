package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.UserReq;
import com.example.demo.dto.res.UserRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.UserService;
import com.example.demo.util.ApiPaging;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/green_earth/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<Object>> getAllUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        try {
            if (ApiPaging.isPagedRequest(q, page, size)) {
                return ResponseHandler.success(
                        (Object) userService.searchUsers(
                                q != null ? q : "",
                                ApiPaging.pageOrZero(page),
                                ApiPaging.sizeBounded(size, 20)
                        ),
                        "Success"
                );
            }
            return ResponseHandler.success((Object) userService.getAllUsers(), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserRes>> findUserById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(userService.findById(id), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<UserRes>> createUser(@RequestBody UserReq req) {
        try {
            return ResponseHandler.success(userService.create(req), "Success");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserRes>> updateUser(@PathVariable Long id, @RequestBody UserReq req) {
        try {
            return ResponseHandler.success(userService.update(id, req), "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseHandler.success("User deleted successfully", "Success");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }
}