package com.zibmbrazil.myFinances.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zibmbrazil.myFinances.model.entity.Releases;

public interface ReleaseRepository extends JpaRepository<Releases, Long> {

}
