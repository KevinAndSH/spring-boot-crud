package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entities.Post;
import com.example.demo.Services.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
  @Autowired
  PostService postService;

  @GetMapping()
  public ArrayList<Post> findAllPosts() {
    return postService.findAll();
  }

  @GetMapping("/query")
  public ArrayList<Post> findPostsByUsername(@RequestParam("user") String username) {
    return postService.findByUsername(username);
  }

  @GetMapping("/{id}")
  public Optional<Post> findPostById(@PathVariable("id") Long id) {
    return postService.findById(id);
  }

  @PostMapping()
  public Post savePost(@RequestBody Post post) {
    return postService.save(post);
  }

  @PutMapping("/{id}")
  public Post updateById(@PathVariable("id") Long id, @RequestBody Post post) {
    return postService.update(id, post.getContent());
  }

  @DeleteMapping("/{id}")
  public boolean deletePostById(@PathVariable("id") Long id) {
    return postService.deleteById(id);
  }
}
