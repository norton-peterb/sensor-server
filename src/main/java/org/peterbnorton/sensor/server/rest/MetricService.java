package org.peterbnorton.sensor.server.rest;

import org.peterbnorton.sensor.server.store.SensorBeanStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricService {

    private final SensorBeanStore sensorBeanStore;

    public MetricService(SensorBeanStore sensorBeanStore) {
        this.sensorBeanStore = sensorBeanStore;
    }

    @GetMapping(path = "/metrics",produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String metrics() {
        return sensorBeanStore.getMetrics();
    }
}
