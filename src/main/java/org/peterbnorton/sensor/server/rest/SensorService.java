package org.peterbnorton.sensor.server.rest;

import org.peterbnorton.sensor.server.model.SensorBean;
import org.peterbnorton.sensor.server.store.SensorBeanStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class SensorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorService.class);

    private final SensorBeanStore sensorBeanStore;

    public SensorService(SensorBeanStore sensorBeanStore) {
        this.sensorBeanStore = sensorBeanStore;
    }

    @PostMapping(path = "/sensor/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sensorSubmit(@RequestBody SensorBean sensorBean) {
        if(sensorBean == null) {
            throw new BadRequestException("Missing SensorBean JSON Data Object");
        }
        List<String> dataErrors = new LinkedList<>();
        if(StringUtils.isEmpty(sensorBean.getSensorId())) {
            dataErrors.add("Missing Sensor ID in JSON Data\n");
        }
        if(StringUtils.isEmpty(sensorBean.getParamName())) {
            dataErrors.add("Missing Parameter Name in JSON Data\n");
        }
        if(StringUtils.isEmpty(sensorBean.getParamValue())) {
            dataErrors.add("Missing Parameter Value in JSON Data\n");
        } else {
            try {
                double paramValue = Double.parseDouble(sensorBean.getParamValue());
                LOGGER.info("Parameter Value Parsed = " + paramValue);
            } catch (NumberFormatException exception) {
                dataErrors.add(exception.getMessage() + "\n");
            }
        }
        if(dataErrors.size() != 0) {
            dataErrors.forEach(LOGGER::error);
            StringBuilder errorMessage = new StringBuilder();
            dataErrors.forEach(errorMessage::append);
            throw new BadRequestException(errorMessage.toString());
        }
        sensorBeanStore.saveSensorBean(sensorBean);
    }
}
