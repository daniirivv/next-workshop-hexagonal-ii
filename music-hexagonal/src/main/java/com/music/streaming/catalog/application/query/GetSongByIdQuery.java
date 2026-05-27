package com.music.streaming.catalog.application.query;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.Song;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder
public class GetSongByIdQuery {
    @NonNull
    final SongRepositoryPort songRepository;
    @NonNull
    final String id;

    public Optional<Song> execute() {
        return songRepository.getSongById(id);
    }
}
