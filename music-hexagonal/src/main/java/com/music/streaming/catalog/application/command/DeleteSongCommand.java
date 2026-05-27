package com.music.streaming.catalog.application.command;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.InvalidSongException;
import com.music.streaming.catalog.domain.SongNotFoundException;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
public class DeleteSongCommand {
    @NonNull
    final SongRepositoryPort songRepository;
    @NonNull
    final String id;

    public void handle() throws InvalidSongException, SongNotFoundException {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidSongException();
        }
        if (songRepository.getSongById(id).isEmpty()) throw new SongNotFoundException();
        songRepository.deleteSong(id);
    }
}
