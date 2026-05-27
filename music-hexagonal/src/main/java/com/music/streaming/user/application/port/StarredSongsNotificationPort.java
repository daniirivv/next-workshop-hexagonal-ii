package com.music.streaming.user.application.port;

public interface StarredSongsNotificationPort {

    void notifyLimitReached(String userId);

}
