package com.example.demo.service;

import com.example.demo.dto.req.ArticleReq;
import com.example.demo.dto.res.ArticleRes;
import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ArticleCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleCategoryRepository articleCategoryRepository;

    public List<ArticleRes> getAllArticles() {
        return articleRepository.findAll().stream().map(ArticleRes::toJson).toList();
    }

    public ArticleRes findById(Long id) {
        return ArticleRes.toJson(articleRepository.findById(id).get());
    }

    public ArticleRes create(ArticleReq req) {
        try {
            Article article = new Article();
            article.setTitle(req.getTitle());
            article.setContent(req.getContent());
            article.setImage(req.getImage());
            article.setAuthor(userRepository.findById(req.getAuthorId()).get());
            article.setCategory(articleCategoryRepository.findById(req.getCategoryId()).get());
            return ArticleRes.toJson(articleRepository.save(article));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ArticleRes update(Long id, ArticleReq req) {
        try {
            Article article = articleRepository.findById(id).get();
            article.setTitle(req.getTitle());
            article.setContent(req.getContent());
            article.setImage(req.getImage());
            article.setAuthor(userRepository.findById(req.getAuthorId()).get());
            article.setCategory(articleCategoryRepository.findById(req.getCategoryId()).get());
            article.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            return ArticleRes.toJson(articleRepository.save(article));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}