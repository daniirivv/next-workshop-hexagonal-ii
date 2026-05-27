package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.StarredSongsNotificationPort;
import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.InvalidStarredException;
import com.music.streaming.user.domain.StarredLimitReachedException;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.domain.UserNotFoundException;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Optional;

@SuperBuilder
public class StarSongCommand {

    @NonNull
    final UserRepositoryPort userRepository;
    @NonNull
    final StarredSongsNotificationPort notificator;
    final String userId;
    final String songId;

    public void handle() throws UserNotFoundException, InvalidStarredException, StarredLimitReachedException {
        Optional<User> user = this.userRepository.getUserById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        User u = user.get();
        List<String> starredSongs = u.getStarredSongIds();
        if(starredSongs.contains(songId)){
            throw new InvalidStarredException();
        }

        if(starredSongs.size() >= User.MAX_STARRED_SONGS){
            notificator.notifyLimitReached(u.getEmail());
            throw new StarredLimitReachedException();
        }

        starredSongs.add(songId);
        userRepository.updateUser(u);
    }

}
