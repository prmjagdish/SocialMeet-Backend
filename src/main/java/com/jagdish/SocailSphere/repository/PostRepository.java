package com.jagdish.SocailSphere.repository;

import com.jagdish.SocailSphere.model.entity.Post;
import com.jagdish.SocailSphere.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
