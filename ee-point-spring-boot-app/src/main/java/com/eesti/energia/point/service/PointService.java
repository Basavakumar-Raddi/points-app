package com.eesti.energia.point.service;

import java.util.List;

import com.eesti.energia.point.dto.PointDTO;
import com.eesti.energia.point.dto.SummaryDto;
import com.eesti.energia.point.entity.Point;

public interface PointService {

    public PointDTO addPoint(PointDTO pointDto);

    public SummaryDto viewPoint();

    public void deletePoint(String id);
}
