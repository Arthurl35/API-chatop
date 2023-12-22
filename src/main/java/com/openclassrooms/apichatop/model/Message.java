package com.openclassrooms.apichatop.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="MESSAGES")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private Timestamp created_at;
	private String message;

	@Column(name = "rental_id")
	private Integer rentalId;
    
	@Column(name = "user_id")
	private Integer userId;
}