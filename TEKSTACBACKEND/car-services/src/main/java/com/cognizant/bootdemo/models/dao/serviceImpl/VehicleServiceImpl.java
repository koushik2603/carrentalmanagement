package com.cognizant.bootdemo.models.dao.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cognizant.bootdemo.models.customException.UserException;
import com.cognizant.bootdemo.models.dao.services.VehicleService;

import com.cognizant.bootdemo.models.pojo.Vehicle;
import com.cognizant.bootdemo.models.repositories.VehicleRepository;


@Service
public class VehicleServiceImpl implements VehicleService {
	@Autowired
	VehicleRepository vehicleRepository;
	
	
	@Override
	public Vehicle saveVehicle(Vehicle veh) {
		// TODO Auto-generated method stub
		Optional<Vehicle> OE=vehicleRepository.findById(veh.getVehicleID());
		if(OE.isPresent()) {
			int id=veh.getVehicleID();
			throw new UserException("Vehicle with ID " + id + " Already Exist");
		}else {
			try {
					Vehicle v=vehicleRepository.save(veh);
					return v;
			}
			catch(DataIntegrityViolationException e) {	
				throw new UserException("Registration Number Already Exist");
			}
		}
	}


	@Override
	public List<Vehicle> getAllVehicle() {
		// TODO Auto-generated method stub
		return vehicleRepository.findAll();
	}


	@Override
	public Vehicle getVehiclebyId(int id) {
		// TODO Auto-generated method stub
		return vehicleRepository.findById(id)
        .orElseThrow(() -> new UserException("Vehicle with ID " + id + " not found"));		
			}


	@Override
	public String deleteVehiclebyId(int id) {
		// TODO Auto-generated method stub
		Optional<Vehicle> OE=vehicleRepository.findById(id);
		if(OE.isPresent()) {
			vehicleRepository.deleteById(id);
			return "Vehicle deleted successfully";
		}else {
			throw  new UserException("Vehicle with ID " + id + " not found");		
		}
		
		
	}


	@Override
	public List<Vehicle> getVehiclebybrand(String brand) {
		// TODO Auto-generated method stub
		List<Vehicle> lt=vehicleRepository.findByBrandContaining(brand);
		if(lt.size()>0) {
		return lt;
		}
		else {
			throw  new UserException("Vehicle with Brand " + brand + " not found");
		}
	}



	@Override
	public String updateVehiclebyId(Vehicle v,int id) {
		Optional<Vehicle> OE=vehicleRepository.findById(id);
		if(OE.isPresent()) {
			vehicleRepository.save(v);
			return "Vehicle with Id"+id+" Update Successfully";
					}
		else {
			throw new UserException("Vehicle with Id "+id+" Does not Exist");

			
		}

		}


	@Override
	public List<Vehicle> getVehicleByRange(int min, int max) {
		// TODO Auto-generated method stub
		
		List<Vehicle> lt=vehicleRepository.findByDailyRateBetween(min, max);
		if(lt.size()>0) {
			return lt;
		}
		else {
			throw new UserException("List is empty");
		}
	}

}
