package com.cognizant.bootdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.bootdemo.models.dao.services.VehicleService;
import com.cognizant.bootdemo.models.pojo.Vehicle;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

	@Autowired
	public VehicleService VehicleServiceImpl;
	
	@PostMapping("/save")
	
	public ResponseEntity<?> SaveEmp(@RequestBody Vehicle veh)  {
		Vehicle v = VehicleServiceImpl.saveVehicle(veh);
		if(v==null) {
			return new ResponseEntity<String>("Vehicle Id already exist",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Vehicle inserted successfully with Id "+veh.getVehicleID(),HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getAllVehicles")
	public ResponseEntity<?> getVehicles(){
		List<Vehicle>list=VehicleServiceImpl.getAllVehicle();
		
		if(list.size()==0)
		{
			return new ResponseEntity<String>("No Vehicle exists",HttpStatus.BAD_REQUEST);
		}
		else 
		{
			return new ResponseEntity<List<Vehicle>>(list,HttpStatus.ACCEPTED);
		}	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getvehiclebyId(@PathVariable int id){
		Vehicle v=VehicleServiceImpl.getVehiclebyId(id);
		if(v==null)
		{
			return new ResponseEntity<String>("No Vehicle exists",HttpStatus.BAD_REQUEST);
		}
		else
		{
			return new ResponseEntity<Vehicle>(v,HttpStatus.ACCEPTED);
		}

	}
	@PutMapping("/updateVehicle/{id}")
	public ResponseEntity<?> VehicleUpdate(@RequestBody Vehicle v,@PathVariable int id){
		return new ResponseEntity<String>(VehicleServiceImpl.updateVehiclebyId(v, id),HttpStatus.ACCEPTED);
	}
	
	
	@DeleteMapping("/deleteVehicle/{id}")
	public	ResponseEntity<?> deleteVehiclebyid(@PathVariable int id){
		return new ResponseEntity<String>(VehicleServiceImpl.deleteVehiclebyId(id),HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{brand}")
	public	ResponseEntity<?> getvehiclebybrand(@PathVariable String brand){
		return new ResponseEntity<List<Vehicle>>(VehicleServiceImpl.getVehiclebybrand(brand),HttpStatus.ACCEPTED);
	}
 
	@GetMapping("/{min}/{max}")
	public ResponseEntity<?> getVehByPriceRange(@PathVariable int min,@PathVariable int max){
		return new ResponseEntity<List<Vehicle>>(VehicleServiceImpl.getVehicleByRange(min, max),HttpStatus.ACCEPTED);
	}
	
}
