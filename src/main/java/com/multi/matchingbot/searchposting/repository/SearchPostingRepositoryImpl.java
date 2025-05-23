//package com.multi.matchingbot.searchposting.repository;
//
//
//import com.multi.matchingbot.jobposting.JobPosting;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class SearchPostingRepositoryImpl implements SearchPostingRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    public List<JobPosting> searchByFilters(String keyword, String title, String skill, String region) {
//        String jpql = "SELECT j FROM JobPosting j WHERE 1=1";
//        Map<String, Object> params = new HashMap<>();
//
//        if (keyword != null && !keyword.isBlank()) {
//            jpql += " AND (j.title LIKE :keyword OR j.description LIKE :keyword)";
//            params.put("keyword", "%" + keyword + "%");
//        }
//
//        if (title != null && !title.isBlank()) {
//            jpql += " AND j.title = :title";
//            params.put("title", title);
//        }
//
//        if (skill != null && !skill.isBlank()) {
//            jpql += " AND j.requiredSkills LIKE :skill";
//            params.put("skill", "%" + skill + "%");
//        }
//
//        if (region != null && !region.isBlank()) {
//            jpql += " AND j.address LIKE :region";
//            params.put("region", "%" + region + "%");
//        }
//
//        TypedQuery<JobPosting> query = em.createQuery(jpql, JobPosting.class);
//        params.forEach(query::setParameter);
//
//        return query.getResultList();
//    }
//}
