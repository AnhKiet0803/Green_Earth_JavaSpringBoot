package com.example.demo.controller;

import com.example.demo.common.ResponseHandler;
import com.example.demo.dto.common.ResponseDTO;
import com.example.demo.dto.req.ArticleReq;
import com.example.demo.dto.res.ArticleRes;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.ArticleService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/green_earth/article")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping()
    public ResponseEntity<ResponseDTO<List<ArticleRes>>> getAllArticles() {
        try {
            return ResponseHandler.success(articleService.getAllArticles(), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ArticleRes>> findArticleById(@PathVariable Long id) {
        try {
            return ResponseHandler.success(articleService.findById(id), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<ArticleRes>> createArticle(@RequestBody ArticleReq req) {
        try {
            return ResponseHandler.success(articleService.create(req), "Thành công");
        } catch (ValidationException v) {
            return ResponseHandler.error(StatusCode.VALIDATION_ERROR, v.getMessage());
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ArticleRes>> updateArticle(@PathVariable Long id, @RequestBody ArticleReq req) {
        try {
            return ResponseHandler.success(articleService.update(id, req), "Thành công");
        } catch (Exception e) {
            return ResponseHandler.error(StatusCode.BAD_REQUEST, e.getMessage());
        }
    }

}