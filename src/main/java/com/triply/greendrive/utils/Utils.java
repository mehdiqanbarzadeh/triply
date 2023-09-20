package com.triply.greendrive.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.triply.greendrive.api.company.dto.request.FileModel;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

	public static String TYPE = "text/csv";

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<FileModel> csvToModel(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			 CSVParser csvParser = new CSVParser(fileReader,
					 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<FileModel> models = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				FileModel fileModel = new FileModel();
				fileModel.setEmployeeId(csvRecord.get(0));
				fileModel.setVehicleIdCode(csvRecord.get(1));
				fileModel.setVehicleType(csvRecord.get(2));
				fileModel.setVehicleMileage(csvRecord.get(3));
				models.add(fileModel);
			}
			return models;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	public static Map<VehicleType, Long> calculateEmissionsByVehicleTypeAndAverageMileage(Map<VehicleType, List<Vehicle>> groupedByType) {
		Map<VehicleType, Long> averageEmissionPerWeek = new HashMap<>();
		for (VehicleType vehicleType : groupedByType.keySet()) {
			int sizeOfRecordByType = groupedByType.get(vehicleType).size();
			Long reduceOfLastWeekMileage = groupedByType.get(vehicleType).stream().map(Vehicle::getMileage).reduce(0L, Long::sum);
			long averageEmission = (reduceOfLastWeekMileage / sizeOfRecordByType) * vehicleType.toDiffusion();
			averageEmissionPerWeek.put(vehicleType, averageEmission);
		}
		return averageEmissionPerWeek;
	}


	public static List<Vehicle> suggestElectricCarReplacements(Map<VehicleType, List<Vehicle>> groupedByType,
			Map<VehicleType, Long> weeklyMileageAverage) {

		List<Vehicle> suggestions = new ArrayList<>();
		for (VehicleType vehicleType : groupedByType.keySet()) {
			List<Vehicle> vehicles = groupedByType.get(vehicleType);
			for (Vehicle vehicle : vehicles) {
				if ((vehicle.getMileage() * vehicle.getType().toDiffusion()) >= weeklyMileageAverage.get(vehicle.getType())) {
					suggestions.add(vehicle);
				}
			}
		}
		return suggestions;
	}

	public static Long calculateAverageEmissions(List<Vehicle> vehicles, VehicleType type) {
		Long reduceOfMileages = vehicles
				.stream()
				.filter(vehicle -> vehicle.getType().equals(type))
				.map(Vehicle::getMileage)
				.reduce(0L, Long::sum);
		return (reduceOfMileages / vehicles.size()) * type.toDiffusion();
	}

	public static Long calculateTotalEmissions(List<Vehicle> vehicles, VehicleType type) {
		Long reduceOfMileages = vehicles
				.stream()
				.filter(vehicle -> vehicle.getType().equals(type))
				.map(Vehicle::getMileage)
				.reduce(0L, Long::sum);
		return reduceOfMileages * type.toDiffusion();
	}

	public static Long calculateTotalMileages(List<Vehicle> vehicles) {
		return vehicles.stream().map(Vehicle::getMileage).reduce(0L, Long::sum);
	}
}