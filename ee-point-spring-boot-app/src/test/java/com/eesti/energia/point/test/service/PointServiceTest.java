package com.eesti.energia.point.test.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eesti.energia.point.dto.PointDTO;
import com.eesti.energia.point.dto.SummaryDto;
import com.eesti.energia.point.entity.Point;
import com.eesti.energia.point.entity.Repository.PointRepository;
import com.eesti.energia.point.info.LocationEnum;
import com.eesti.energia.point.service.PointService;
import com.eesti.energia.point.service.PointServiceImpl;
import com.eesti.energia.point.util.DateUtil;
import com.eesti.energia.point.util.mapper.PointMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PointServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public PointService pointService() {
            return new PointServiceImpl();
        }
    }

    @Autowired
    private PointService pointService;

    @MockBean
    private PointRepository pointRepository;

    @MockBean
    private PointMapper pointMapper;

    PointDTO pointDTO = new PointDTO();

    @Before
    public void setUp() {
        Point point = new Point();
        point.setValue(200.35);
        point.setLocation(LocationEnum.EE);
        point.setMeasurementDay(1545004800000L);
        point.setCreatedBy("testUser");
        point.setCreatedDate(new Date());

        pointDTO.setMeasurementDay(DateUtil.getStringFromMillis(point.getMeasurementDay()));
        pointDTO.setValue(point.getValue());
        pointDTO.setLocation(point.getLocation());

        List<Point> pointList = new ArrayList<>();
        pointList.add(point);

        Mockito.when(pointRepository.findByMeasurementDayAndLocation(point.getMeasurementDay(),point.getLocation()))
                .thenReturn(point);
        Mockito.when(pointRepository.save(point)).thenReturn(point);
        Mockito.when(pointRepository.findAll()).thenReturn(pointList);
        Mockito.when(pointMapper.mapEntityToDto(point)).thenReturn(pointDTO);
        Mockito.when(pointMapper.mapDtoToEntity(pointDTO)).thenReturn(point);

    }

    @Test
    public void viewPointTest() {
        Double value = 200.35;
        SummaryDto summaryDto = pointService.viewPoint();

        Assert.assertEquals(summaryDto.getMaxValue(),value);
        Assert.assertEquals(summaryDto.getPointDTOS().get(0).getLocation(),LocationEnum.EE);
        Assert.assertEquals(summaryDto.getPointDTOS().get(0).getMeasurementDay(),"2018-12-17");
    }

    @Test
    public void failViewPointTest() {
        Double value = 8.35;
        SummaryDto summaryDto = pointService.viewPoint();

        Assert.assertNotEquals(summaryDto.getMaxValue(),value);
        Assert.assertNotEquals(summaryDto.getPointDTOS().get(0).getLocation(),LocationEnum.FI);
        Assert.assertNotEquals(summaryDto.getPointDTOS().get(0).getMeasurementDay(),"2018-11-18");
    }

    @Test
    public void addPointTest() {
        Double value = 200.35;
        PointDTO pointDTOResp = pointService.addPoint(pointDTO);
        Assert.assertEquals(pointDTOResp.getMeasurementDay(),"2018-12-17");
        Assert.assertEquals(pointDTOResp.getLocation(),LocationEnum.EE);
        Assert.assertEquals(pointDTOResp.getValue(),value);

    }

    @Test
    public void failAddPointTest() {
        Double value = 88.98;
        PointDTO pointDTOResp = pointService.addPoint(pointDTO);
        Assert.assertNotEquals(pointDTOResp.getMeasurementDay(),"2018-05-10");
        Assert.assertNotEquals(pointDTOResp.getLocation(),LocationEnum.FI);
        Assert.assertNotEquals(pointDTOResp.getValue(),value);

    }

}
