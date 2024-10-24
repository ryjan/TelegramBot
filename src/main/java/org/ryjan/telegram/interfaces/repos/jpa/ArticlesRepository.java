package org.ryjan.telegram.interfaces.repos.jpa;

import org.ryjan.telegram.model.users.Articles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    List<Articles> findFirst10ByOrderByIdAsc();
    List<Articles> findFirst10ByStatusOrderByIdAsc(String status);
    List<Articles> findAllByUserId(Long userId);
    List<Articles> findAllByUsername(String username);
}
