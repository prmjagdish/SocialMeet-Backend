package com.jagdish.SocialMeet.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
}

