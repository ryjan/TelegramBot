package org.ryjan.telegram.services;

import org.ryjan.telegram.interfaces.repos.jpa.ArticlesRepository;
import org.ryjan.telegram.model.users.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticlesService extends ServiceBuilder {

    @Autowired
    private ArticlesRepository articlesRepository;

    public List<Articles> getFirstTenArticles() {
        return articlesRepository.findFirst10ByOrderByIdAsc();
    }

    public void update(Articles articles) {
        articlesRepository.save(articles);
    }

    public void delete(Articles articles) {
        articlesRepository.delete(articles);
    }
}
