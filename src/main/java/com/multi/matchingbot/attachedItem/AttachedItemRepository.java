package com.multi.matchingbot.attachedItem;

import com.multi.matchingbot.attachedItem.domain.AttachedItem;
import com.multi.matchingbot.common.domain.enums.ItemType;
import com.multi.matchingbot.common.domain.enums.Yn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachedItemRepository extends JpaRepository<AttachedItem, Long> {
    /**
     *
     * @param referenceId attachedItem 테이블에 저장된 참조 아이디(기업 회원 아이디)
     * @param itemType 첨부파일형식(enum)
     * @param yn 삭제상태(enum)
     * @return AttachedItem 엔티티를 Optional객체에 담아서 리턴
     */
    Optional<AttachedItem> findByReferenceIdAndItemTypeAndStatus(Long referenceId, ItemType itemType, Yn yn);
}
