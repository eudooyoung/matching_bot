package com.multi.matchingbot.member.repository;

import com.multi.matchingbot.member.domain.CareerType;
import com.multi.matchingbot.member.domain.entities.Resume;
import com.multi.matchingbot.resume.repository.ResumeRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ResumeRepositoryImpl implements ResumeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Resume> searchWithFilters(String jobGroup, String jobType, String jobRole, String careerType, String companyName, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT r FROM Resume r JOIN r.occupation o JOIN r.member m JOIN r.careers c WHERE 1=1");


        List<Object> params = new ArrayList<>();
        List<String> paramNames = new ArrayList<>();

        if (jobGroup != null && !jobGroup.isEmpty()) {
            jpql.append(" AND o.jobGroupName = ?").append(params.size() + 1);
            params.add(jobGroup);
        }
        if (jobType != null && !jobType.isEmpty()) {
            jpql.append(" AND o.jobTypeName = ?").append(params.size() + 1);
            params.add(jobType);
        }
        if (jobRole != null && !jobRole.isEmpty()) {
            jpql.append(" AND o.jobRoleName = ?").append(params.size() + 1);
            params.add(jobRole);
        }
        if (careerType != null && !careerType.isEmpty()) {
            jpql.append(" AND c.careerType = ?").append(params.size() + 1);
            params.add(CareerType.valueOf(careerType)); // enum 필드 매핑
        }

        if (companyName != null && !companyName.isEmpty()) {
            jpql.append(" AND c.companyName LIKE ?").append(params.size() + 1);
            params.add("%" + companyName + "%");
        }

        TypedQuery<Resume> query = em.createQuery(jpql.toString(), Resume.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }

        // 페이징 적용
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Resume> resultList = query.getResultList();

        // Count Query (중복 제거)
        String countJpql = jpql.toString().replaceFirst("SELECT DISTINCT r", "SELECT COUNT(DISTINCT r)");
        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        for (int i = 0; i < params.size(); i++) {
            countQuery.setParameter(i + 1, params.get(i));
        }
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}
