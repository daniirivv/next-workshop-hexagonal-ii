package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.StarredSongsNotificationPort;
import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.*;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Optional;

@SuperBuilder
public class UnstarSongCommand {

    @NonNull
    final UserRepositoryPort userRepository;
    final String userId;
    final String songId;

    public void handle() throws UserNotFoundException, InvalidStarredException {
        Optional<User> user = this.userRepository.getUserById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        User u = user.get();
        List<String> starredSongs = u.getStarredSongIds();
        if(!starredSongs.contains(songId)){
            throw new InvalidStarredException();
        }

        starredSongs.remove(songId);
        userRepository.updateUser(u);
    }

}
