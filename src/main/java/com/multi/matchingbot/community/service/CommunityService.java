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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        CommunityPost post = postRepo.findByIdWithComments(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        post.setViews(post.getViews() + 1);
        postRepo.save(post);
        return post;
    }

    public void createPost(CommunityPostDto dto, Member member) {
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

    @Transactional
    public void deletePost(Long postId, Member member) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        log.info("🔍 삭제 시도 - 게시글 ID: {}, 요청자 ID: {}, 작성자 ID: {}",
                postId, member.getId(), post.getMember().getId());

        if (!post.getMember().getId().equals(member.getId())) {
            log.warn("❌ 삭제 권한 없음");
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        postRepo.delete(post);
        log.info("✅ 게시글 삭제 성공 - ID: {}", postId);
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

    @Transactional
    public void updateComment(Long commentId, String content, Member member) {
        CommunityComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 본인 댓글인지 확인
        if (!comment.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }

        comment.setContent(content);
        comment.setUpdatedBy(member.getName());
        comment.setUpdatedAt(java.time.LocalDateTime.now());

        // commentRepo.save(comment);  // 선택적: JPA의 영속성 컨텍스트 내에서 자동 반영됨
    }

    public Long getPostIdByCommentId(Long commentId) {
        CommunityComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
        return comment.getPost().getId();
    }


}
