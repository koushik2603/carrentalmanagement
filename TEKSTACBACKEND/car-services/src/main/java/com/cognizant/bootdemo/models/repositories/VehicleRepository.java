package com.cognizant.bootdemo.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognizant.bootdemo.models.pojo.Vehicle;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
	List<Vehicle> findByBrandContaining(String model);
	List<Vehicle> findByDailyRateBetween(int min, int max);
}
