package com.fakelsmile.quote.service.models.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity for vote.
 */
@Entity
@Table(name = "votes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"user", "quote"})
@EqualsAndHashCode(callSuper = true, exclude = {"user", "quote"})
public class VoteEntity extends BaseEntity {

    /**
     * The date and time when the vote was last updated.
     */
    @LastModifiedDate
    @Column(name = "update_at")
    private Instant updateAt;

    /**
     * The user who made the vote.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity user;

    /**
     * The quote for which the vote was made.
     */
    @ManyToOne
    @JoinColumn(name = "quote_id", nullable = false, referencedColumnName = "id")
    private QuoteEntity quote;

    /**
     * The type of vote.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type")
    private VoteType voteType;

    /**
     * Indicates whether the vote is active or inactive.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
