package com.example.demo.Entities;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  private Long id;

  private String content;
  private String username;
  private LocalDateTime postedAt;
  private LocalDateTime updatedAt;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LocalDateTime getPostedAt() {
    return this.postedAt;
  }

  public void setPostedAt(LocalDateTime postedAt) {
    this.postedAt = postedAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  private void validateContent() {
    if (content.isBlank()) {
      throw new RuntimeException("Post content cannot be empty or null");
    }
  }

  private void validateUsername() {
    if (username.isBlank()) {
      throw new RuntimeException("Username cannot be empty or null");
    }
  }

  public void validatePost() {
    validateContent();
    validateUsername();
  }
}
