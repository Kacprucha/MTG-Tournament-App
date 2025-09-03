package com.example.backend.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService 
{
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void sendDelayedNotification(String destination, Object payload, long delayMillis) {
        try 
        {
            Thread.sleep(delayMillis);
            
            messagingTemplate.convertAndSend(destination, payload);
            
        } 
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }
    }
}
