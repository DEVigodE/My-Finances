package com.zibmbrazil.myFinances.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zibmbrazil.myFinances.model.entity.Release;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

}
