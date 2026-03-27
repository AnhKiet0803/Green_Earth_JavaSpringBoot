package com.example.demo.dto.res;

import com.example.demo.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class ArticleRes {
    private Long id;
    private String title;
    private String content;
    private String image;
    private Long authorId;
    private Long categoryId;
    private String categoryName;
    private Timestamp createdAt;

    public static ArticleRes toJson(Article article) {
        return new ArticleRes(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getImage(),
                article.getAuthor().getId(),
                article.getCategory() != null ? article.getCategory().getId() : null,
                article.getCategory() != null ? article.getCategory().getName() : "There are no categories",
                article.getCreatedAt()

        );
    }
}
