package org.example.service;

import org.example.model.Post;
import org.example.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setAnonymous(postDTO.isAnonymous());
        post.setUserId(postDTO.getUserId());
        post.setCreatedAt(new Date());

        //        CommunityActivityScore score = scoreRepository.findByUserId(post.getUserId())
//                .orElseGet(() -> {
//                    CommunityActivityScore newScore = new CommunityActivityScore();
//                    newScore.setUserId(post.getUserId());
//                    return newScore;
//                });
//
//        score.incrementPostCount();
//        scoreRepository.save(score);

        return postRepository.save(post);
    }

//    public Post createPost(Post post) {
//        post.setCreatedAt(new Date());
//        Post savedPost = postRepository.save(post);
//
//        CommunityActivityScore score = scoreRepository.findByUserId(post.getUserId())
//                .orElseGet(() -> {
//                    CommunityActivityScore newScore = new CommunityActivityScore();
//                    newScore.setUserId(post.getUserId());
//                    return newScore;
//                });
//
//        score.incrementPostCount();
//        scoreRepository.save(score);
//
//        return savedPost;
//    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post updatePost(Long id, Post updatedPost) {
        if (postRepository.existsById(id)) {
            updatedPost.setId(id);
            return postRepository.save(updatedPost);
        }
        return null;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}