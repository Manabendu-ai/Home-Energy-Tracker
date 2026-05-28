package riku.spring.usage_service.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import riku.spring.kafka.event.AlertingEvent;
import riku.spring.kafka.event.EnergyUsageEvent;
import riku.spring.usage_service.dto.DeviceResponse;
import riku.spring.usage_service.dto.UserResponse;
import riku.spring.usage_service.model.DeviceEnergyUsage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsageService {

    private final InfluxDBClient influxDBClient;
    @Value("${influx.bucket}")
    private String influxBucket;
    @Value("${influx.org}")
    private String influxOrg;

    private final DeviceService deviceService;
    private final UserService userService;

    private final KafkaTemplate<String, AlertingEvent> kafkaTemplate;

    @KafkaListener(topics = "energy-usage", groupId = "usage-service")
    public void energyUsageEvent(EnergyUsageEvent energyUsageEvent){
        log.info("Received Energy Usage Event {}", energyUsageEvent);

        Point point = Point.measurement("energy_usage")
                .addTag("deviceId", String.valueOf(energyUsageEvent.deviceId()))
                .addField("energyConsumed", energyUsageEvent.energyConsumed())
                .time(energyUsageEvent.timestamp(), WritePrecision.MS);
        influxDBClient.getWriteApiBlocking().writePoint(influxBucket, influxOrg, point);
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void aggregateDeviceEnergyUsage(){
        final Instant now = Instant.now();
        final Instant oneHourAgo = now.minusSeconds(3600);
        String fluxQuery = String.format("""
                from(bucket: "%s")
                |> range(start: time(v: "%s"), stop: time(v: "%s"))
                |> filter(fn: (r) => r["_measurement"] == "energy_usage")
                |> filter(fn: (r) => r["_field"] == "energyConsumed")
                |> group(columns: ["deviceId"])
                |> sum(column: "_value")
                """
                , influxBucket, oneHourAgo.toString(), now);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, influxOrg);
        List<DeviceEnergyUsage> deviceEnergies = new ArrayList<>();
        for(FluxTable table : tables){
            for(FluxRecord record : table.getRecords()){
                String deviceIdStr = (String) record.getValueByKey("deviceId");
                Double energyConsumed = record.getValueByKey("_value") instanceof Number ?
                        ((Number) record.getValueByKey("_value")).doubleValue() : 0.0;

                deviceEnergies.add(
                        DeviceEnergyUsage.builder()
                                .deviceId(Long.valueOf(deviceIdStr))
                                .energyConsumed(energyConsumed)
                                .build()
                );
            }
        }
        log.info("Aggregated device energies over the past hour: {}", deviceEnergies);

        for(DeviceEnergyUsage deviceEnergy : deviceEnergies){
            final DeviceResponse device  = deviceService.getDeviceDto(deviceEnergy.getDeviceId());
            if(device==null || deviceEnergy.getDeviceId() == null){
                log.warn("Device with ID : {} Not found!", deviceEnergy.getDeviceId());
                continue;
            }
            deviceEnergy.setUserId(device.getUserId());
        }
        deviceEnergies.removeIf(x -> x.getUserId()==null);

        Map<Long, List<DeviceEnergyUsage>> userDeviceMap = deviceEnergies.stream().collect(
                Collectors.groupingBy(DeviceEnergyUsage::getUserId)
        );

        log.info("User Device Map : {}", userDeviceMap);

        List<Long> userIds = new ArrayList<>(userDeviceMap.keySet());
        final Map<Long, Double> userThresholdMap = new HashMap<>();
        final Map<Long, String> userEmailMap = new HashMap<>();

        for(final long userId : userIds){
            try{
                UserResponse user = userService.getUserById(userId);
                if(user==null || (user.getId() == null) || !user.isAlerting()){
                    log.warn("User with ID : {} Not found! or Alerting disabled!", userId);
                    continue;
                }
                userThresholdMap.put(user.getId(), user.getThreshold());
                userEmailMap.put(user.getId(), user.getEmail());
            } catch (Exception e){
                log.warn("Failed to fetch user fo IDS: {}",userId);
            }
        }

        final List<Long> alertedUsers = new ArrayList<>(userThresholdMap.keySet());
        for(final Long userId : alertedUsers){
            final Double threshold = userThresholdMap.get(userId);
            final List<DeviceEnergyUsage> devices = userDeviceMap.get(userId);

            final Double totalConsumption = devices.stream()
                    .mapToDouble(DeviceEnergyUsage::getEnergyConsumed)
                    .sum();

            if(totalConsumption > threshold){
                log.info("ALERT: User ID {} has exceeded the energy threshold! " +
                                "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold);

                final AlertingEvent alertingEvent = AlertingEvent.builder()
                        .userId(userId)
                        .message("Energy Consumption Threshold Exceeded!")
                        .threshold(threshold)
                        .email(userEmailMap.get(userId))
                        .build();

                kafkaTemplate.send("energy-alerts",alertingEvent);
            }

        }
    }
}
