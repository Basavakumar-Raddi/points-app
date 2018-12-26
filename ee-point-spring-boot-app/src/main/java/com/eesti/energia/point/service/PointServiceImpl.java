package com.eesti.energia.point.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.eesti.energia.point.dto.PointDTO;
import com.eesti.energia.point.dto.SummaryDto;
import com.eesti.energia.point.entity.Point;
import com.eesti.energia.point.entity.Repository.PointRepository;
import com.eesti.energia.point.util.mapper.PointMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl implements PointService{

    @Inject
    private PointRepository pointRepository;

    @Autowired
    private PointMapper pointMapper;

    @Override public PointDTO addPoint(PointDTO pointDto) {
        Point pointSaved = null;
        Point point = pointMapper.mapDtoToEntity(pointDto);

        Point existingPoint = pointRepository.findByMeasurementDayAndLocation(point.getMeasurementDay(), point.getLocation());

        if(existingPoint != null){
            existingPoint.setValue(existingPoint.getValue() + point.getValue());
            existingPoint.setLastModifiedBy("user");
            existingPoint.setLastModifiedDate(new Date());
            pointSaved = pointRepository.save(existingPoint);
        } else {
            point.setCreatedBy("user");
            point.setCreatedDate(new Date());
            pointSaved = pointRepository.save(point);
        }

        return pointMapper.mapEntityToDto(pointSaved);
    }

    @Override public SummaryDto viewPoint() {
        List<Point> pointList = pointRepository.findAll();
        /*List<PointDTO> response = ConversionUtil.convertJson(ConversionUtil.convertToJsonString(pointList),
                new TypeReference<List<PointDTO>>(){});*/
        return getPointDtoList(pointList);
    }

    @Override public void deletePoint(String id) {
        pointRepository.deleteById(id);
    }

    private SummaryDto getPointDtoList(List<Point> pointList){
        SummaryDto summaryDto = new SummaryDto();
        List<PointDTO> pointDTOS = new ArrayList<>();
        for(Point point : pointList){
            pointDTOS.add(pointMapper.mapEntityToDto(point));
        }

        DoubleSummaryStatistics stats = pointDTOS.stream()
                .mapToDouble((x) -> x.getValue())
                .summaryStatistics();

        summaryDto.setPointDTOS(pointDTOS);
        summaryDto.setAverageValue(stats.getAverage());
        summaryDto.setMaxValue(stats.getMax());
        summaryDto.setMinValue(stats.getMin());
        summaryDto.setTotalValue(stats.getSum());
        return summaryDto;
    }
}
