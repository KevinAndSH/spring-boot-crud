package com.example.demo;

import java.util.ArrayList;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Controllers.PostController;
import com.example.demo.Entities.Post;
import com.example.demo.Services.PostService;
import com.github.javafaker.Faker;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class DemoApplicationTests {

	private Faker faker = new Faker();
	private Post createPost() {
		Post newPost = new Post();
		newPost.setContent(faker.lorem().paragraph());
		newPost.setUsername(faker.lordOfTheRings().character());

		return newPost;
	}

	@Autowired
	private PostService postService;

	@Autowired
	private PostController postController;

	@BeforeAll
	void cleanup() {
		postService.dangerouslyDeleteEverySinglePostSavedInTheDatabase();
	}

	@Test
	@DisplayName("GET /posts - should return an array of posts")
	void findAllTest() {
		ArrayList<Post> posts = postController.findAllPosts();
		Assertions.assertTrue(posts instanceof ArrayList<Post>);
	}
	
	@Test
	@DisplayName("POST /posts - should save a post")
	void saveTest() {
		Post newPost = createPost();

		Post savedPost = postController.savePost(newPost);

		Assertions.assertEquals(newPost.getUsername(), savedPost.getUsername());
		Assertions.assertEquals(newPost.getContent(), savedPost.getContent());
	}

	@Test
	@DisplayName("GET /posts/query?user={user} - should send posts with the given username")
	void findByUserTest() {
		Post newPost = createPost();
		String username = newPost.getUsername();

		postController.savePost(newPost);

		ArrayList<Post> posts = postController.findPostsByUsername(username);
		Assertions.assertTrue(posts.size() >= 1);

		for (Post post : posts) {
			Assertions.assertEquals(post.getUsername(), username);
		}
	}

	@Test
	@DisplayName("POST /posts - should send the post saved before")
	void getSavedTest() {
		Post newPost = createPost();

		Post savedPost = postController.savePost(newPost);

		Post savedPostData = new Post();
		savedPostData.setContent(savedPost.getContent());
		savedPostData.setUsername(savedPost.getUsername());

		Assertions.assertEquals(newPost.getContent(), savedPostData.getContent());
		Assertions.assertEquals(newPost.getUsername(), savedPostData.getUsername());
	}

	@Test
	@DisplayName("POST /posts - should send an error if the body is incomplete and not save")
	void sendBadRequestTest() {
		Post goodPost = createPost();

		Post emptyPost = new Post();
		emptyPost.setUsername(goodPost.getUsername());

		Post anonymousPost = new Post();
		anonymousPost.setContent(goodPost.getContent());

		Integer postsAmount = postController.findAllPosts().size();

		Assertions.assertThrows(RuntimeException.class, () -> {
			postController.savePost(emptyPost);
		});

		Assertions.assertThrows(RuntimeException.class, () -> {
			postController.savePost(anonymousPost);
		});

		Assertions.assertEquals(postsAmount, postController.findAllPosts().size());
	}

	@Test
	@DisplayName("GET /posts/{id} - should send a specific post given an id")
	void findByIdTest() {
		Post newPost = createPost();
		Post savedPost = postController.savePost(newPost);

		Post sentPost = postController.findPostById(savedPost.getId()).orElse(null);

		Assertions.assertNotNull(sentPost);
		Assertions.assertEquals(savedPost.getContent(), sentPost.getContent());
		Assertions.assertEquals(savedPost.getUsername(), sentPost.getUsername());
	}

	@Test
	@DisplayName("GET /posts/{id} - should send an error when given an invalid id")
	void findByBadIdTest() {
		Post ghostPost = postController.findPostById(Long.valueOf(0)).orElse(null);
		Assertions.assertNull(ghostPost);
	}

	@Test
	@DisplayName("PUT /posts/{id} - should edit an existing post")
	void editTest() {
		Post newPost = createPost();

		Post savedPost = postController.savePost(newPost);

		Post anotherPost = createPost();
		Post editedPost = postController.updateById(savedPost.getId(), anotherPost);

		Assertions.assertEquals(savedPost.getId(), editedPost.getId());
		Assertions.assertEquals(anotherPost.getContent(), editedPost.getContent());
	}

	@Test
	@DisplayName("PUT /posts/{id} - should throw an error when no content is sent")
	void badEditTest() {
		Post newPost = createPost();
		
		Post savedPost = postController.savePost(newPost);
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			postController.updateById(savedPost.getId(), new Post());
		});
	}

	@Test
	@DisplayName("PUT /posts/{id} - should send an error if there's no post with the given id")
	void editNullTest() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			postController.updateById(Long.valueOf(0), createPost());
		});
	}

	@Test
	@DisplayName("DELETE /posts/{id} - should delete a post given an id, and send false when it doesn't exist")
	void deleteTest() {
		Post newPost = createPost();

		Post savedPost = postController.savePost(newPost);

		Assertions.assertTrue(postController.deletePostById(savedPost.getId()));
		Assertions.assertFalse(postController.deletePostById(savedPost.getId()));
	}
}
