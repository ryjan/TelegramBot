package org.ryjan.telegram.commands.users.user.button.bugreport;

import org.ryjan.telegram.model.users.Articles;
import org.ryjan.telegram.repos.jpa.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugReportService {

    @Autowired
    ArticlesRepository articlesRepository;

    public List<Articles> findArticlesList(Long userId) {
        return articlesRepository.findAllByUserId(userId);
    }

    public List<Articles> findArticlesList(String username) {
        return articlesRepository.findAllByUsername(username);
    }

    public void update(Articles articles) {
        articlesRepository.save(articles);
    }

    public void delete(Articles articles) {
        articlesRepository.delete(articles);
    }
}
