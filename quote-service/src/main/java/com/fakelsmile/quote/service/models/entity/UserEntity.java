package com.fakelsmile.quote.service.models.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity for user.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"votes", "quotes"})
@ToString(callSuper = true, exclude = {"votes", "quotes"})
public class UserEntity extends BaseEntity {

    /**
     * The name of the user.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * The email address of the user.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The password of the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The quotes created by the user.
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<QuoteEntity> quotes = new ArrayList<>();

    /**
     * The votes made by the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VoteEntity> votes = new ArrayList<>();
}
