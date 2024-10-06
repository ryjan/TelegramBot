package org.ryjan.telegram.repos.jpa;

import org.ryjan.telegram.model.users.Articles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    List<Articles> findAllByUserId(Long userId);
    List<Articles> findAllByUsername(String username);
}
