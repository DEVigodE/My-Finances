package com.zibmbrazil.myFinances.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zibmbrazil.myFinances.model.entity.Release;
import com.zibmbrazil.myFinances.model.enums.Status;
import com.zibmbrazil.myFinances.model.enums.TypeRelease;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
	
	@Query( value = "select sum(l.value) from Release l  join l.user u where u.id = :idUser and l.type = :type and l.status = :status group by u" )
	BigDecimal getBalanceByTypeReleaseAndUser( @Param("idUser") Long idUser, @Param("type") TypeRelease type, @Param("status") Status status);

}
