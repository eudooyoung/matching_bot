package com.multi.matchingbot.community.service;

import com.multi.matchingbot.community.domain.CommunityCategory;
import com.multi.matchingbot.community.domain.CommunityComment;
import com.multi.matchingbot.community.domain.CommunityPost;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.repository.CommunityCategoryRepository;
import com.multi.matchingbot.community.repository.CommunityCommentRepository;
import com.multi.matchingbot.community.repository.CommunityPostRepository;
import com.multi.matchingbot.member.domain.entities.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityPostRepository postRepo;
    private final CommunityCommentRepository commentRepo;
    private final CommunityCategoryRepository categoryRepo;

    public List<CommunityPostDto> getPostsByCategory(Long categoryId) {
        List<CommunityPost> posts = (categoryId == null)
                ? postRepo.findAll()
                : postRepo.findByCategoryId(categoryId);

        return posts.stream()
                .map(CommunityPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CommunityPost getPostWithComments(Long postId) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        post.setViews(post.getViews() + 1);
        postRepo.save(post);
        return post;
    }

    public void savePost(CommunityPostDto dto, Member member) {
        CommunityCategory category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        CommunityPost post = new CommunityPost();
        post.setCategory(category);
        post.setMember(member);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedBy(member.getName());
        post.setCreatedAt(java.time.LocalDateTime.now());

        postRepo.save(post);
    }

    public void updatePost(Long postId, CommunityPostDto dto, Member member) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        CommunityCategory category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(category);
        post.setUpdatedBy(member.getName());
        post.setUpdatedAt(java.time.LocalDateTime.now());
        postRepo.save(post);
    }

    public void deletePost(Long postId, Member member) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postRepo.delete(post);
    }

    public void addComment(Long postId, String content, Member member) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        CommunityComment comment = new CommunityComment();
        comment.setPost(post);
        comment.setMember(member);
        comment.setContent(content);
        comment.setCreatedBy(member.getName());
        comment.setCreatedAt(java.time.LocalDateTime.now());

        commentRepo.save(comment);
    }

    public List<CommunityCategory> getAllCategories() {
        return categoryRepo.findAll();
    }
}