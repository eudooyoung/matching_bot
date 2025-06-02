package com.multi.matchingbot.attachedItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachedItemRepository extends JpaRepository<AttachedItem, Long> {
}
