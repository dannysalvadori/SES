package com.fdmgroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
