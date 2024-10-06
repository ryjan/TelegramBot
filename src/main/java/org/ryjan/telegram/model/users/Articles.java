package org.ryjan.telegram.model.users;

import jakarta.persistence.*;

@Entity
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

    public Articles() {

    }

    public Articles(String title, String text, String username, Long userId) {
        this.title = title;
        this.text = text;
        this.username = username;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
