package com.multi.matchingbot.admin.controller;

import com.multi.matchingbot.admin.domain.AdminPagedResult;
import com.multi.matchingbot.admin.domain.AdminSearchCondition;
import com.multi.matchingbot.admin.domain.view.*;
import com.multi.matchingbot.admin.service.AdminBoardService;
import com.multi.matchingbot.community.domain.CommunityCommentDto;
import com.multi.matchingbot.community.domain.CommunityPost;
import com.multi.matchingbot.community.domain.CommunityPostDto;
import com.multi.matchingbot.community.repository.CommunityCategoryRepository;
import com.multi.matchingbot.community.service.CommunityService;
import com.multi.matchingbot.company.domain.Company;
import com.multi.matchingbot.company.service.CompanyService;
import com.multi.matchingbot.job.domain.dto.JobDto;
import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.job.service.JobService;
import com.multi.matchingbot.member.domain.dto.MemberProfileUpdateDto;
import com.multi.matchingbot.member.domain.entity.Member;
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/board")
public class AdminBoardController {

    private final AdminBoardService adminPageService;
    private final JobService jobService;
    private final CompanyService companyService;
    private final MemberService memberService;
    private final ResumeService resumeService;
    private final CommunityService communityService;

    private final CommunityCategoryRepository communityCategoryRepository;

    /**
     * 개인 회원 관리자 게시판 페이지 매핑
     *
     * @param condition     검색 조건
     * @param bindingResult 유효성 검증 처리용 객체
     * @param model         디티오 전달용 객체
     * @return 관리자 페이지 반환
     */
    @GetMapping("/members")
    public String members(@Validated @ModelAttribute AdminSearchCondition condition,
                          BindingResult bindingResult, Model model) {

        model.addAttribute("condition", condition);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "검색어는 50자 이하로 입력하세요");
            return "/admin/board-members";
        }

        AdminPagedResult<MemberAdminView> result = adminPageService.members(condition);
        model.addAttribute("members", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());

        return "/admin/board-members";
    }

    /**
     * 개인 회원 상세 페이지 이동
     *
     * @param memberId
     * @param model
     * @return
     */
    @GetMapping("/members/{memberId}")
    public String adminMemberDetail(@PathVariable(name = "memberId") Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        MemberProfileUpdateDto dto = new MemberProfileUpdateDto();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setName(member.getName());
        dto.setNickname(member.getNickname());
        dto.setPhone(member.getPhone());
        dto.setGender(member.getGender());
        dto.setBirth(member.getBirth());

        // address → addressRegion, addressDetail 나누기 (예시: "서울시 강남구 역삼동 123-4")
        if (member.getAddress() != null) {
            String[] parts = member.getAddress().split(" ", 2);
            dto.setAddress(parts[0]); // addressRegion 대체
            if (parts.length > 1) {
                dto.setAddressDetail(parts[1]);
            }
        }

        model.addAttribute("member", dto);
        return "member/profile-edit";
    }

    /**
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/companies")
    public String companies(@Validated @ModelAttribute AdminSearchCondition condition,
                            BindingResult bindingResult, Model model) {
        model.addAttribute("condition", condition);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "검색어는 50자 이하로 입력하세요");
            return "/admin/board-companies";
        }

        AdminPagedResult<CompanyAdminView> result = adminPageService.companies(condition);
        model.addAttribute("companies", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());

        return "/admin/board-companies";
    }

    /**
     * 기업 홈페이지 매핑
     *
     * @param companyId
     * @param model
     * @return
     */
    @GetMapping("/companies/{companyId}")
    public String adminCompanyHome(@PathVariable(name = "companyId") Long companyId, Model model) {
        Company company = companyService.findById(companyId);
        Page<JobDto> jobPage = jobService.getByCompanyIdPaged(companyId, PageRequest.of(0, 20));
        model.addAttribute("companyId", companyId);
        model.addAttribute("company", company);
        model.addAttribute("jobPage", jobPage);
        return "company/index";
    }

    /**
     * 기업 회원 정보 매핑
     *
     * @param companyId
     * @param model
     * @return
     */
    @GetMapping("companies/edit-profile/{companyId}")
    public String adminCompanyDetail(@PathVariable(name = "companyId") Long companyId, Model model) {
        model.addAttribute("company", companyService.findById(companyId));
        return "company/edit-profile";
    }

    /**
     * 이력서 관리 게시판 매핑
     *
     * @param condition 검색조건
     * @param model
     * @return
     */
    @GetMapping("/resumes")
    public String resumes(@Validated @ModelAttribute AdminSearchCondition condition,
                          BindingResult bindingResult, Model model) {
        model.addAttribute("condition", condition);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "검색어는 50자 이하로 입력하세요");
            return "/admin/board-resumes";
        }

        AdminPagedResult<ResumeAdminView> result = adminPageService.resumes(condition);
        model.addAttribute("resumes", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());

        return "/admin/board-resumes";
    }

    /**
     * 이력서 상세 페이지 매핑
     *
     * @param resumeId 이력서 아이디
     * @param model    객체 전달용 모델
     * @return 상세 페이지 경로
     */
    @GetMapping("/resumes/{resumeId}")
    public String adminResumeDetail(@PathVariable(name = "resumeId") Long resumeId, Model model) {
        Resume resume = resumeService.findById(resumeId);
        model.addAttribute("resume", resume);
        return "resume/detail";
    }

    /**
     * 채용 공고 관리 게시판 매핑
     *
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/jobs")
    public String jobs(@Validated @ModelAttribute AdminSearchCondition condition,
                       BindingResult bindingResult, Model model) {
        model.addAttribute("condition", condition);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "검색어는 50자 이하로 입력하세요");
            return "/admin/board-jobs";
        }

        AdminPagedResult<JobAdminView> result = adminPageService.jobs(condition);
        model.addAttribute("jobs", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());

        return "/admin/board-jobs";
    }

    /**
     * 채용 공고 상세 페이지 매핑
     *
     * @param jobId
     * @param model
     * @return
     */
    @GetMapping("/jobs/{jobId}")
    public String adminJobDetail(@PathVariable(name = "jobId") Long jobId, Model model) {
        Job job = jobService.findById(jobId);
        Long postingCompanyId = job.getCompany().getId();
        model.addAttribute("job", job);
        model.addAttribute("companyId", postingCompanyId);
        return "job/job-detail";
    }

    /**
     * 커뮤니티 관리 게시판 매핑
     *
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/community")
    public String communityPosts(@Validated @ModelAttribute AdminSearchCondition condition,
                                 BindingResult bindingResult, Model model) {
        model.addAttribute("condition", condition);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "검색어는 50자 이하로 입력하세요");
            return "/admin/board-community";
        }

        AdminPagedResult<CommunityAdminView> result = adminPageService.posts(condition);
        model.addAttribute("posts", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());

        return "/admin/board-community";
    }

    @GetMapping("/community/{communityPostId}")
    public String adminCommunityPostDetail(@PathVariable(name = "communityPostId") Long communityPostId,
                                           Model model,
                                           Authentication authentication) {
        CommunityPost post = communityService.getPostWithComments(communityPostId);
        model.addAttribute("post", CommunityPostDto.fromEntity(post));
        model.addAttribute("categories", communityService.getAllCategories());
        model.addAttribute("comment", post.getComments().stream()
                .map(CommunityCommentDto::fromEntity)
                .toList());

        // ✅ 관리자 여부 전달
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // ✅ currentUserId 설정
        if (authentication != null) {
            String email = authentication.getName();
            Long currentUserId;
            try {
                Member member = memberService.findByUsername(email);
                currentUserId = member.getId();
            } catch (Exception e) {
                Company company = companyService.findByEmail(email);
                currentUserId = company.getId();
            }
            model.addAttribute("currentUserId", currentUserId);
        } else {
            model.addAttribute("currentUserId", null);
        }

        return "community/community-detail";
    }

}
