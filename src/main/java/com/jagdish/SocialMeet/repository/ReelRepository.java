package com.jagdish.SocialMeet.repository;

import com.jagdish.SocialMeet.model.entity.Reel;
import com.jagdish.SocialMeet.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReelRepository extends JpaRepository<Reel, Long> {
    List<Reel> findByUser(User user);
}
