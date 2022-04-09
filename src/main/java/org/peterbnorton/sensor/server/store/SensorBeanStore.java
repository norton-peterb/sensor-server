package org.peterbnorton.sensor.server.store;

import org.peterbnorton.sensor.server.model.SensorBean;

import java.util.List;

public interface SensorBeanStore extends MetricStore {
    void saveSensorBean(SensorBean sensorBean);
    boolean invalidateSensorBean(String sensorId,String paramName);
    List<SensorBean> getForParamName(String paramName);
    void performAutoInvalidationCheck();
}
