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
import com.multi.matchingbot.member.service.MemberService;
import com.multi.matchingbot.resume.domain.entity.Resume;
import com.multi.matchingbot.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/members")
    public String members(@ModelAttribute AdminSearchCondition condition, Model model) {
        AdminPagedResult<MemberAdminView> result = adminPageService.members(condition);
        model.addAttribute("members", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
        return "/admin/board-members";
    }

    //    구직자 회원 정보 임시 매핑
    @GetMapping("/members/{memberId}")
    public String adminMemberDetail(@PathVariable(name = "memberId") Long memberId, Model model) {
        model.addAttribute("memberId", memberService.getMemberById(memberId));
        return "members/profile";
    }

    /**
     * @param condition
     * @param model
     * @return
     */
    @GetMapping("/companies")
    public String companies(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("statusParam = [{}]", condition.getStatus());
        AdminPagedResult<CompanyAdminView> result = adminPageService.companies(condition);
        model.addAttribute("companies", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
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
    public String resumes(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("statusParam = [{}]", condition.getKeywordsStatus());
        AdminPagedResult<ResumeAdminView> result = adminPageService.resumes(condition);
        model.addAttribute("resumes", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);
        System.out.println("careerType: " + condition.getCareerType());
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
    public String jobs(@ModelAttribute AdminSearchCondition condition, Model model) {
        log.warn("jobSearchParam = [{}]", condition);
        AdminPagedResult<JobAdminView> result = adminPageService.jobs(condition);

        model.addAttribute("jobs", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);

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
    public String communityPosts(@ModelAttribute AdminSearchCondition condition, Model model) {
        AdminPagedResult<CommunityAdminView> result = adminPageService.posts(condition);

        model.addAttribute("posts", result.getPage().getContent());
        model.addAttribute("page", result.getPage());
        model.addAttribute("pageNumbers", result.getPageNumbers());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("condition", condition);

        model.addAttribute("categories", communityCategoryRepository.findAll());

        return "/admin/board-community";
    }

    @GetMapping("/community/{communityPostId}")
    public String adminCommunityPostDetail(@PathVariable(name = "communityPostId") Long communityPostId, Model model) {
        CommunityPost post = communityService.getPostWithComments(communityPostId);
        model.addAttribute("post", CommunityPostDto.fromEntity(post));
        model.addAttribute("categories", communityService.getAllCategories());
        model.addAttribute("comment", post.getComments().stream()
                .map(CommunityCommentDto::fromEntity)
                .toList());

        return "community/community-detail";
    }
}
