package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.StarredSongsNotificationPort;
import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.SongAlreadyStarredException;
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
    final StarredSongsNotificationPort notificator;
    final String userId;
    final String songId;

    public void handle() throws UserNotFoundException, SongAlreadyStarredException {
        Optional<User> user = this.userRepository.getUserById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        User u = user.get();
        List<String> starredSongs = u.getStarredSongIds();
        if(starredSongs.contains(songId)){
            throw new SongAlreadyStarredException();
        }

        starredSongs.add(songId);
        userRepository.updateUser(u);
    }

}
