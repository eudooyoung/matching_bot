package com.multi.matchingbot.community.service;

import com.multi.matchingbot.community.domain.CommunityCategory;
import com.multi.matchingbot.community.domain.CommunityComment;
import com.multi.matchingbot.community.domain.CommunityPost;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.repository.CommunityCategoryRepository;
import com.multi.matchingbot.community.repository.CommunityCommentRepository;
import com.multi.matchingbot.community.repository.CommunityPostRepository;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.company.domain.Company;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        post.setCreatedBy("member" + (member.getId())); // ✅ ID 저장
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
        post.setUpdatedBy("member" + (member.getId())); // ✅ ID 저장

        post.setUpdatedAt(java.time.LocalDateTime.now());
        postRepo.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Member member) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        if (post.getMember() == null) {
            log.warn("❌ 게시글에 작성자 정보가 없습니다. postId: {}", postId);
            throw new IllegalStateException("게시글에 작성자 정보가 없습니다.");
        }

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
        comment.setCreatedBy(String.valueOf(member.getId())); // ✅ ID 저장
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


    public Page<CommunityPostDto> getPagedPosts(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<CommunityPost> postPage = (categoryId == null)
                ? postRepo.findAll(pageable)
                : postRepo.findByCategoryId(categoryId, pageable);

        List<CommunityPostDto> postDtos = postPage.stream()
                .map(CommunityPostDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(postDtos, pageable, postPage.getTotalElements());
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        log.info("🧪 댓글 삭제 시도 - commentId: {}, memberId: {}", commentId, memberId);

        CommunityComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));

        log.info("✅ 댓글 존재 확인됨 - 작성자 ID: {}", comment.getMember().getId());

        if (!comment.getMember().getId().equals(memberId)) {
            log.warn("❌ 삭제 권한 없음 - 요청자 ID: {}, 작성자 ID: {}", memberId, comment.getMember().getId());
            throw new AccessDeniedException("본인이 작성한 댓글만 삭제할 수 있습니다");
        }

        commentRepo.delete(comment);
        log.info("✅ 댓글 삭제 실행됨 - commentId: {}", commentId);
    }

    public void createPostByCompany(CommunityPostDto dto, Company company) {
        CommunityCategory category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        CommunityPost post = new CommunityPost();
        post.setCategory(category);
        post.setCompany(company);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedBy(company.getName());  // 기업 이름으로 작성자 표시
        post.setCreatedAt(java.time.LocalDateTime.now());

        postRepo.save(post);
    }
    public void addCommentByCompany(Long postId, String content, Company company) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        CommunityComment comment = new CommunityComment();
        comment.setPost(post);
        comment.setCompany(company);
        comment.setContent(content);
        comment.setCreatedBy(company.getName());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepo.save(comment);
    }
    // 게시글 수정 (기업)
    @Transactional
    public void updatePostByCompany(Long postId, CommunityPostDto dto, Company company) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (post.getCompany() == null || !post.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다.");
        }

        CommunityCategory category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(category);
        post.setUpdatedBy(company.getName());
        post.setUpdatedAt(LocalDateTime.now());

        postRepo.save(post);
    }

    // 게시글 삭제 (기업)
    @Transactional
    public void deletePostByCompany(Long postId, Company company) {
        CommunityPost post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        if (post.getCompany() == null || !post.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        postRepo.delete(post);
    }

    // 댓글 수정 (기업)
    @Transactional
    public void updateCommentByCompany(Long commentId, String content, Company company) {
        CommunityComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        if (comment.getCompany() == null || !comment.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }

        comment.setContent(content);
        comment.setUpdatedBy(company.getName());
        comment.setUpdatedAt(LocalDateTime.now());
    }

    // 댓글 삭제 (기업)
    @Transactional
    public void deleteCommentByCompany(Long commentId, Long companyId) {
        CommunityComment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        if (comment.getCompany() == null || !comment.getCompany().getId().equals(companyId)) {
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }

        commentRepo.delete(comment);
    }

    public void deleteCommentAsAdmin(Long commentId) {
        commentRepo.deleteById(commentId);
    }
}

