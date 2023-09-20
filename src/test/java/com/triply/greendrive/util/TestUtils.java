package com.triply.greendrive.util;

import com.triply.greendrive.api.authentication.dto.request.AuthenticationRequest;
import com.triply.greendrive.model.company.Company;
import com.triply.greendrive.model.user.Role;
import com.triply.greendrive.model.user.User;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

public class TestUtils {


    public static HttpHeaders getDefaultHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public static String baseUrl(int port) {
        return "http://localhost:" + port;
    }

    public static AuthenticationRequest getAuthenticationRequest() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("Test@123123");
        return request;
    }

    public static Company getValidCompany(String name, String code) {
        Company company = new Company();
        company.setName(name);
        company.setCode(code);
        return company;
    }

    public static User getValidUser() {
        User user = new User();
        user.setRole(Role.ADMIN);
        user.setLastName("lastname");
        user.setFirstName("firstName");
        user.setEmail("email@gmail.com");
        user.setPassword("dsfgsdfgsdfg7s6769");
        return user;
    }

    public static AuthenticationRequest getValiAuthRequest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("email@gmail.com");
        authenticationRequest.setPassword("pass");
        return authenticationRequest;
    }

    public static List<Vehicle> getValidVehicles() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setEmployeeId("E1");
        vehicle1.setIDCode("C1");
        vehicle1.setMileage(100L);
        vehicle1.setType(VehicleType.VAN);
        vehicle1.setCompany(getValidCompany("name", "code"));

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setEmployeeId("E2");
        vehicle2.setIDCode("C2");
        vehicle2.setMileage(1000L);
        vehicle2.setType(VehicleType.VAN);
        vehicle2.setCompany(getValidCompany("name", "code"));
        return Arrays.asList(vehicle1, vehicle2);
    }
}
