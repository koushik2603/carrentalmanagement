package com.cognizant.bootdemo.models.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name="tblVehicle_Inventory")
public class Vehicle {
	@Id
	private int vehicleID;
	
	@Column(nullable = false,length=30)
	private String vehicleName;
	
	@Column(nullable = false,length=30)
	private String brand;
	
	@Column(nullable = false,length=30)
	private String model;
	
	@Column(unique = true,nullable = false,length=30)
	private String regNumber;
	
	@Column(nullable = false)
	private int dailyRate;
	
	
	@Column(nullable = false,length=5)
	private String isAvailable;
	
	private String pictureUrl;
 
}
