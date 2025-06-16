package com.multi.matchingbot.searchposting.repository;


import com.multi.matchingbot.job.domain.entity.Job;
import com.multi.matchingbot.mapposting.domain.MapPostingDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Repository
@RequiredArgsConstructor
public class SearchPostingRepositoryImpl implements SearchPostingRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Job> findById(Long id) {
        Job result = em.find(Job.class, id);
        return Optional.ofNullable(result);
    }
    // SearchPostingRepositoryImpl.java
    @Override
    public Page<MapPostingDto> findAllMapPostings(Pageable pageable) {
        String jpql = "SELECT m FROM MapPosting m";
        List<Job> resultList = em.createQuery(jpql, Job.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<MapPostingDto> dtoList = resultList.stream()
                .map(MapPostingDto::fromEntity)
                .toList();

        // 전체 개수도 필요함
        Long count = em.createQuery("SELECT COUNT(m) FROM MapPosting m", Long.class)
                .getSingleResult();

        return new org.springframework.data.domain.PageImpl<>(dtoList, pageable, count);
    }


    @Override
    public List<Job> searchByFilters(String jobGroup, String jobType, String jobRole, String regionMain, String regionSub,String companyName) {
        String jpql = "SELECT m FROM Job m " +
                "JOIN m.occupation o " +
                "JOIN m.company c " +
                "WHERE 1=1";


        if (jobGroup != null && !jobGroup.isEmpty()) {
            jpql += " AND o.jobGroupName = :jobGroup";
        }
        if (jobType != null && !jobType.isEmpty()) {
            jpql += " AND o.jobTypeName = :jobType";
        }
        if (jobRole != null && !jobRole.isEmpty()) {
            jpql += " AND o.jobRoleName = :jobRole";
        }

        if (regionMain != null && !regionMain.isEmpty()) {
            jpql += " AND m.address LIKE CONCAT('%', :regionMain, '%')";
        }

        if (regionSub != null && !regionSub.isEmpty() && !regionSub.equals("전지역")) {
            jpql += " AND m.address LIKE CONCAT('%', :regionSub, '%')";
        }
        if (companyName != null && !companyName.isEmpty()) {
            jpql += " AND c.name LIKE CONCAT('%', :companyName, '%')";

        }


        TypedQuery<Job> query = em.createQuery(jpql, Job.class);

        if (regionMain != null && !regionMain.isEmpty()) {
            query.setParameter("regionMain", regionMain);
        }
        if (regionSub != null && !regionSub.isEmpty() && !regionSub.equals("전지역")) {
            query.setParameter("regionSub", regionSub);
        }

        if (jobGroup != null && !jobGroup.isEmpty()) {
            query.setParameter("jobGroup", jobGroup);
        }
        if (jobType != null && !jobType.isEmpty()) {
            query.setParameter("jobType", jobType);
        }
        if (jobRole != null && !jobRole.isEmpty()) {
            query.setParameter("jobRole", jobRole);
        }
        if (companyName != null && !companyName.isEmpty()) query.setParameter("companyName", companyName);


        return query.getResultList();
    }

}
