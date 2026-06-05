package riku.spring.alert_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import riku.spring.kafka.event.AlertingEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final MailService mailService;

    @KafkaListener(topics = "energy-alerts", groupId = "alert-service")
    public void energyUsageAlertEvent(AlertingEvent alertingEvent){
        log.info("Received Alerting event: {} ",alertingEvent);

        final String subject = "Energy Usage Alert for user: "+alertingEvent.getUserId();
        final String  message = "Alert: "+alertingEvent.getMessage()+"\n"
                + "Threshold: "+alertingEvent.getThreshold()+"\n"
                +"Energy Consumed: "+alertingEvent.getEnergyConsumed();
        mailService.sendEmail(
                alertingEvent.getEmail(),
                subject,
                message,
                alertingEvent.getUserId()
        );
    }
}
