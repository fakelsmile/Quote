package com.fakelsmile.quote.service.models.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity for quote.
 */
@Entity
@Table(name = "quotes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"author", "votes"})
@EqualsAndHashCode(callSuper = true, exclude = {"author", "votes"})
public class QuoteEntity extends BaseEntity {

    /**
     * The content of the quote.
     */
    @Column(nullable = false)
    private String content;

    /**
     * The score of the quote.
     */
    @Column(nullable = false)
    private Long score;

    /**
     * The time of the last update for the quote.
     */
    @LastModifiedDate
    @Column(name = "update_at")
    private Instant updateAt;

    /**
     * The author of the quote
     */
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, referencedColumnName = "id")
    private UserEntity author;

    /**
     * The votes for the quote.
     */
    @OneToMany(mappedBy = "quote", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VoteEntity> votes = new ArrayList<>();

}
