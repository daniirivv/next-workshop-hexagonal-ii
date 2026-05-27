package com.music.streaming.catalog.application.command;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.InvalidSongException;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.domain.SongNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSongCommandTest {

    @Mock
    SongRepositoryPort songRepository;

    @Test
    void handle_shouldSucceedWhenValid() {
        String id = UUID.randomUUID().toString();
        when(songRepository.getSongById(id)).thenReturn(Optional.of(Song.builder().id(id).build()));

        assertDoesNotThrow(() ->
                DeleteSongCommand.builder()
                        .songRepository(songRepository)
                        .id(id)
                        .build().handle());
        verify(songRepository).deleteSong(id);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenIdIsNotValidUUID() {
        assertThrows(InvalidSongException.class, () ->
                DeleteSongCommand.builder()
                        .songRepository(songRepository)
                        .id("invalid-id")
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowSongNotFoundExceptionWhenSongDoesNotExist() {
        String id = UUID.randomUUID().toString();
        when(songRepository.getSongById(id)).thenReturn(Optional.empty());

        assertThrows(SongNotFoundException.class, () ->
                DeleteSongCommand.builder()
                        .songRepository(songRepository)
                        .id(id)
                        .build().handle());
        verify(songRepository, never()).deleteSong(any());
    }
}
