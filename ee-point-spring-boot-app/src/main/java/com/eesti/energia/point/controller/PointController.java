package com.eesti.energia.point.controller;

import javax.inject.Inject;

import com.eesti.energia.point.dto.PointDTO;
import com.eesti.energia.point.dto.SummaryDto;
import com.eesti.energia.point.service.PointService;
import com.eesti.energia.point.util.ConversionUtil;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
public class PointController {

    @Inject
    private PointService pointService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/addPoint")
    public ApiResponse<PointDTO> addPoint(@RequestBody String pointRequest) {
        ApiResponse<PointDTO> response = new ApiResponse<>();
        try {
            PointDTO pointDto = ConversionUtil.convertJson(pointRequest, PointDTO.class);
            response.setResponse(pointService.addPoint(pointDto));
            response.setSuccess(Boolean.TRUE);
            response.setMessage("Point added successfully");
        } catch (Exception e) {
            log.error("error occurred during add point", e);
            response.setSuccess(Boolean.FALSE);
            response.setMessage("error occurred while adding the point");
        }
        return response;
    }

    @GetMapping("/viewPoints")
    public ApiResponse<SummaryDto> viewPoints() {
        ApiResponse<SummaryDto> response = new ApiResponse<>();
        try {
            response.setResponse(pointService.viewPoint());
            response.setSuccess(Boolean.TRUE);
            response.setMessage("Points fetched successfully");
        } catch (Exception e) {
            log.error("error occurred while fetch", e);
            response.setSuccess(Boolean.FALSE);
            response.setMessage("error occurred while fetching the points");
        }
        return response;
    }

    @DeleteMapping("/deletePoint")
    public ApiResponse<PointDTO> deletePoint(@RequestParam String id) {
        ApiResponse<PointDTO> response = new ApiResponse<>();
        try {
            pointService.deletePoint(id);
            response.setSuccess(Boolean.TRUE);
            response.setMessage("Point deleted successfully");
        } catch (Exception e) {
            log.error("error occurred during delete point", e);
            response.setSuccess(Boolean.FALSE);
            response.setMessage("error occurred while deleting the point");
        }
        return response;
    }
}