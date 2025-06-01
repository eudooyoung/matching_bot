package com.multi.matchingbot.common.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attached_itmes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachedItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long referenceId;
}
