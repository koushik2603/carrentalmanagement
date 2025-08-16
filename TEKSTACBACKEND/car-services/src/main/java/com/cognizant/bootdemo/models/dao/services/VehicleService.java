package com.cognizant.bootdemo.models.dao.services;

import com.cognizant.bootdemo.models.pojo.Vehicle;
import java.util.List;
public interface VehicleService {
	Vehicle saveVehicle(Vehicle v);
	List<Vehicle>getAllVehicle();
	Vehicle getVehiclebyId(int id);
	String  deleteVehiclebyId(int id);
	List<Vehicle> getVehiclebybrand(String brand);
	String updateVehiclebyId(Vehicle v,int id);
	List<Vehicle> getVehicleByRange(int one,int two);
}

