package com.browndwarf.noticeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.browndwarf.noticeboard.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

}
