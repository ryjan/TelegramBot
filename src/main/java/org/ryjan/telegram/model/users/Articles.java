package org.ryjan.telegram.model.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "articles_seq")
    @SequenceGenerator(
            name = "articles_seq",
            sequenceName = "articles_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String text;
    private String username;
    private Long userId;
    private String status;
    private String createdAt;

    public Articles() {

    }

    public Articles(String title, String text, String username, Long userId) {
        this.title = title;
        this.text = text;
        this.username = username;
        this.userId = userId;
        this.createdAt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
    }
}
