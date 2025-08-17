package com.jagdish.SocailSphere.repository;

import com.jagdish.SocailSphere.model.entity.Reel;
import com.jagdish.SocailSphere.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReelRepository extends JpaRepository<Reel, Long> {
    List<Reel> findByUser(User user);
}
