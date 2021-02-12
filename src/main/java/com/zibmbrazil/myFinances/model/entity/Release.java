package com.zibmbrazil.myFinances.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.zibmbrazil.myFinances.model.enums.Status;
import com.zibmbrazil.myFinances.model.enums.TypeRelease;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "releases", schema = "finances")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Release {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "description")
	private String description;

	@Column(name = "month")
	private Integer month;

	@Column(name = "year")
	private Integer year;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	@Column(name = "value")
	private BigDecimal value;

	@Column(name = "type")
	@Enumerated(value = EnumType.STRING)
	private TypeRelease type;

	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private Status status;

	@Column(name = "date_register")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDate dateRegister;

}
