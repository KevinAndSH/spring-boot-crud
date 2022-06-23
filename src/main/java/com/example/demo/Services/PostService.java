package com.example.demo.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.Post;
import com.example.demo.Repositories.PostRepository;

@Service
public class PostService {
  @Autowired
  PostRepository postRepo;

  public ArrayList<Post> findAll() {
    return (ArrayList<Post>) postRepo.findAll();
  }

  public Optional<Post> findById(Long id) {
    return postRepo.findById(id);
  }

  public ArrayList<Post> findByUsername(String username) {
    return postRepo.findByUsername(username);
  }

  public Post save(Post post) {
    post.validatePost();

    post.setPostedAt(LocalDateTime.now());
    post.setUpdatedAt(LocalDateTime.now());

    return postRepo.save(post);
  }

  public Post update(Long id, String content) {
    Post post = findById(id).orElse(null);

    if (post == null) {
      throw new RuntimeException("Post with the given id doesn't exist");
    }

    if (content.isEmpty()) {
      throw new RuntimeException("No content was sent");
    }

    post.setContent(content);
    post.setUpdatedAt(LocalDateTime.now());
    save(post);
    return post;
  }
  
  public boolean deleteById(Long id) {
    try {
      postRepo.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void dangerouslyDeleteEverySinglePostSavedInTheDatabase() {
    postRepo.deleteAll();
  }
}
