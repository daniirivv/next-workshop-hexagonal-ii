package com.music.streaming.user.application.query;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.domain.SongNotFoundException;
import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.domain.UserNotFoundException;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuperBuilder
public class GetAllStarredSongByUserIdQuery {
    @NonNull
    private UserRepositoryPort userRepository;
    @NonNull
    private SongRepositoryPort songRepository;

    public List<Song> execute(String userId) throws UserNotFoundException {
        Optional<User> user = userRepository.getUserById(userId);
        if(user.isEmpty()) {
            throw new UserNotFoundException();
        }

        List<String> ids  = user.get().getStarredSongIds();
        List<Song> songs = new ArrayList<>();
        for(var id : ids) songRepository.getSongById(id).ifPresent(songs::add);
        return songs;
    }
}
