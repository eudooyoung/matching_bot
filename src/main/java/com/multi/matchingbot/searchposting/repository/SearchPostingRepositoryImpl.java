package com.multi.matchingbot.searchposting.repository;

import com.multi.matchingbot.mapposting.domain.MapPosting;
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
    public Optional<MapPosting> findById(Long id) {
        MapPosting result = em.find(MapPosting.class, id);
        return Optional.ofNullable(result);
    }
    // SearchPostingRepositoryImpl.java
    @Override
    public Page<MapPostingDto> findAllMapPostings(Pageable pageable) {
        String jpql = "SELECT m FROM MapPosting m";
        List<MapPosting> resultList = em.createQuery(jpql, MapPosting.class)
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
    public List<MapPosting> searchByFilters(String keyword, String title, String skill, String region) {
        String jpql = "SELECT m FROM MapPosting m WHERE 1=1";

        if (keyword != null && !keyword.isEmpty()) {
            jpql += " AND (m.title LIKE CONCAT('%', :keyword, '%') OR m.requiredSkills LIKE CONCAT('%', :keyword, '%'))";
        }
        if (title != null && !title.isEmpty()) {
            jpql += " AND m.title = :title";
        }
        if (skill != null && !skill.isEmpty()) {
            jpql += " AND m.requiredSkills LIKE CONCAT('%', :skill, '%')";
        }
        if (region != null && !region.isEmpty()) {
            jpql += " AND m.address LIKE CONCAT('%', :region, '%')";
        }

        TypedQuery<MapPosting> query = em.createQuery(jpql, MapPosting.class);

        if (keyword != null && !keyword.isEmpty()) query.setParameter("keyword", keyword);
        if (title != null && !title.isEmpty()) query.setParameter("title", title);
        if (skill != null && !skill.isEmpty()) query.setParameter("skill", skill);
        if (region != null && !region.isEmpty()) query.setParameter("region", region);

        return query.getResultList();
    }
}
