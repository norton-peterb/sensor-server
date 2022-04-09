package org.peterbnorton.sensor.server.controller;

import org.peterbnorton.sensor.server.rest.BadRequestException;
import org.peterbnorton.sensor.server.store.SensorBeanStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    private static final String PARAM_NAME_TEMP = "env_temperature";
    private static final String PARAM_NAME_HUMIDITY = "env_humidity";
    private static final String PARAM_NAME_PRESSURE = "env_pressure";
    private static final String PARAM_NAME_AIR_QUALITY = "env_air_quality";

    private final SensorBeanStore sensorBeanStore;

    public MainController(SensorBeanStore sensorBeanStore) {
        this.sensorBeanStore = sensorBeanStore;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute(PARAM_NAME_TEMP,sensorBeanStore.getForParamName(PARAM_NAME_TEMP));
        model.addAttribute(PARAM_NAME_HUMIDITY,sensorBeanStore.getForParamName(PARAM_NAME_HUMIDITY));
        model.addAttribute(PARAM_NAME_PRESSURE,sensorBeanStore.getForParamName(PARAM_NAME_PRESSURE));
        model.addAttribute(PARAM_NAME_AIR_QUALITY,sensorBeanStore.getForParamName(PARAM_NAME_AIR_QUALITY));
        return "index";
    }

    @RequestMapping("/sensor/invalidate/{paramName}/{sensorId}")
    public String sensorInvalidate(@PathVariable String paramName, @PathVariable String sensorId, RedirectAttributes redirectAttributes) {
        if(!sensorBeanStore.invalidateSensorBean(sensorId,paramName)) {
            throw new BadRequestException("Requested SensorInvalidateBean Data not found");
        }
        redirectAttributes.addFlashAttribute(PARAM_NAME_TEMP,sensorBeanStore.getForParamName(PARAM_NAME_TEMP));
        redirectAttributes.addFlashAttribute(PARAM_NAME_HUMIDITY,sensorBeanStore.getForParamName(PARAM_NAME_HUMIDITY));
        redirectAttributes.addFlashAttribute(PARAM_NAME_PRESSURE,sensorBeanStore.getForParamName(PARAM_NAME_PRESSURE));
        redirectAttributes.addFlashAttribute(PARAM_NAME_AIR_QUALITY,sensorBeanStore.getForParamName(PARAM_NAME_AIR_QUALITY));
        return "redirect:/";
    }
}
