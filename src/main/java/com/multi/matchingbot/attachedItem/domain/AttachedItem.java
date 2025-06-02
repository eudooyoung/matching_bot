package com.multi.matchingbot.attachedItem.domain;

import com.multi.matchingbot.common.domain.entity.BaseEntity;
import com.multi.matchingbot.common.domain.enums.ItemType;
import com.multi.matchingbot.common.domain.enums.Yn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attached_items",
        indexes = {@Index(name = "idx_ref_type", columnList = "referenceId, itemType")}
)
public class AttachedItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String systemName;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn status;

}
