package com.multi.matchingbot.common.domain.entity;

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
public class AttachedItems extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType type;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String systemName;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Yn status;

}
