 package cn.tyreplus.guanglong.inventory.tasks;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
    
// 	TODO: Monitor SMS Server Weekly, Need to be a cron schedule
	@Scheduled(initialDelay=604800000, fixedDelay = 604800000)
    public void smsServerHeartBeat () {

    }

}
