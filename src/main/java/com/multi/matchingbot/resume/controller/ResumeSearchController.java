//import com.multi.matchingbot.common.security.MBotUserDetails;
//import com.multi.matchingbot.member.domain.dto.ResumeDto;
//import com.multi.matchingbot.member.domain.entity.Resume;
//import com.multi.matchingbot.member.service.ResumeService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequestMapping("/resumes")
//public class ResumeSearchController {
//
//    private final ResumeService resumeService;
//
//    public ResumeSearchController(ResumeService resumeService) {
//        this.resumeService = resumeService;
//    }
//
//
////    @GetMapping
////    public String searchResumes(
////            @RequestParam(required = false) String jobGroup,
////            @RequestParam(required = false) String jobType,
////            @RequestParam(required = false) String jobRole,
////            @RequestParam(required = false) String careerType,
////            @RequestParam(required = false) String companyName,
////            @RequestParam(defaultValue = "1") int page,
////            Model model
////    ) {
////        Pageable pageable = PageRequest.of(page - 1, 6);  // 페이지는 0부터 시작
////        Page<ResumeDto> resumeList = resumeService.searchResumes(jobGroup, jobType, jobRole, careerType, companyName, pageable);
////
////        model.addAttribute("resumeList", resumeList.getContent());
////        model.addAttribute("currentPage", page);
////        model.addAttribute("totalPages", resumeList.getTotalPages());
////
////        return "member/member-resume-list";  // 검색 결과 뷰 재활용
////    }
//@GetMapping
//public String searchResumes(
//        @RequestParam(required = false) String jobGroup,
//        @RequestParam(required = false) String jobType,
//        @RequestParam(required = false) String jobRole,
//        @RequestParam(required = false) String careerType,
//        @RequestParam(required = false) String companyName,
//        @RequestParam(defaultValue = "1") int page,
//        @AuthenticationPrincipal MBotUserDetails user,
//        Model model
//) {
//    Pageable pageable = PageRequest.of(page - 1, 6);
//    Page<ResumeDto> resumeList = resumeService.searchResumes(jobGroup, jobType, jobRole, careerType, companyName, pageable);
//
//    model.addAttribute("resumeList", resumeList.getContent());
//    model.addAttribute("currentPage", page);
//    model.addAttribute("totalPages", resumeList.getTotalPages());
//
//    // ✅ role 추가
//    if (user != null) {
//        model.addAttribute("role", user.getRole());
//    } else {
//        model.addAttribute("role", "GUEST");
//    }
//
//    return "member/member-resume-list";
//}
//
//
////    @GetMapping("/{id}")
////    public String resumeDetail(@PathVariable("id") Long id, Model model) {
////        Resume resume = resumeService.findByIdWithOccupation(id);
////        model.addAttribute("resume", resume);
////        return "member/resume-view";  // 상세 뷰 재활용
////    }
//@GetMapping("/{id}")
//public String resumeDetail(
//        @PathVariable("id") Long id,
//        @AuthenticationPrincipal MBotUserDetails user,
//        Model model
//) {
//    Resume resume = resumeService.findByIdWithOccupation(id);
//    model.addAttribute("resume", resume);
//
//    // ✅ role 추가
//    if (user != null) {
//        model.addAttribute("role", user.getRole());
//    } else {
//        model.addAttribute("role", "GUEST");
//    }
//
//    return "member/resume-view";
//}
//
//}
