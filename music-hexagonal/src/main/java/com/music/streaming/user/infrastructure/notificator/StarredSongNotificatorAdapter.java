package com.music.streaming.user.infrastructure.notificator;

import com.music.streaming.user.application.port.StarredSongsNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StarredSongNotificatorAdapter implements StarredSongsNotificationPort {

    final String MESSAGE = "LÍMITE ALCANZADO";
    @Override
    public void notifyLimitReached(String email) {
        System.out.println(MESSAGE);
    }
}
