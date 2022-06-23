package com.example.demo.Repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
  public abstract ArrayList<Post> findByUsername(String username);
}
