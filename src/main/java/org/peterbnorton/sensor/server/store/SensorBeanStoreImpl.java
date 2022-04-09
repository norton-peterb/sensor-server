package org.peterbnorton.sensor.server.store;

import org.peterbnorton.sensor.server.model.SensorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SensorBeanStoreImpl implements SensorBeanStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorBeanStoreImpl.class);

    private final Map<String,SensorBean> sensorBeanMap = new HashMap<>();
    private final Map<String, Date> autoInvalidateMap = new HashMap<>();

    @Override
    public void saveSensorBean(SensorBean sensorBean) {
        String key = sensorBean.getSensorId() + ":" + sensorBean.getParamName();
        LOGGER.info("Adding Entry {}",key);
        sensorBeanMap.put(key,sensorBean);
        autoInvalidateMap.put(key,new Date());
    }

    @Override
    public String getMetrics() {
        StringBuilder stringBuilder = new StringBuilder();
        sensorBeanMap.forEach(
                (k,v) -> {
                    stringBuilder.append(v.getParamName());
                    stringBuilder.append("{sensorId=\"");
                    stringBuilder.append(v.getSensorId());
                    stringBuilder.append("\"} ");
                    stringBuilder.append(v.getParamValue());
                    stringBuilder.append("\n");
                }
        );
        return stringBuilder.toString();
    }

    @Override
    public boolean invalidateSensorBean(String sensorId, String paramName) {
        String key = sensorId + ":" + paramName;
        return invalidateSensorBean(key);
    }

    private boolean invalidateSensorBean(String key) {
        LOGGER.info("Removing Entry {}",key);
        autoInvalidateMap.remove(key);
        SensorBean sensorBean = sensorBeanMap.remove(key);
        return sensorBean != null;
    }

    @Override
    public List<SensorBean> getForParamName(String paramName) {
        List<SensorBean> sensorBeans = new LinkedList<>();
        sensorBeanMap.forEach(
                (k,v) -> {
                    if(k.contains(paramName)) {
                        sensorBeans.add(v);
                    }
                }
        );
        return sensorBeans;
    }

    @Override
    @Scheduled(fixedDelay = 60000L)
    public void performAutoInvalidationCheck() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-1);
        Date expiryDate = calendar.getTime();
        LOGGER.info("ExpiryDate is {}",expiryDate);
        List<String> keysToRemove = new LinkedList<>();
        autoInvalidateMap.forEach(
                (k,v) -> {
                    if (v.before(expiryDate)) {
                        LOGGER.info("Key {} has expired with value {}",k,v);
                        keysToRemove.add(k);
                    }
                }
        );
        keysToRemove.forEach(
                this::invalidateSensorBean
        );
    }
}
