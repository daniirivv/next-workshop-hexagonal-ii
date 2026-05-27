package com.music.streaming.catalog.application.command;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.InvalidSongException;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.domain.SongDomainService;
import com.music.streaming.catalog.domain.SongNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateSongCommandTest {

    @Mock
    SongRepositoryPort songRepository;

    SongDomainService songDomainService = new SongDomainService();

    @Test
    void handle_shouldSucceedWhenValid() {
        String id = UUID.randomUUID().toString();
        when(songRepository.getSongById(id))
                .thenReturn(Optional.of(Song.builder().id(id).title("Old Title").durationSeconds(100).build()));

        assertDoesNotThrow(() ->
                UpdateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .id(id)
                        .title("New Title")
                        .durationSeconds(240)
                        .artistId("artist-1")
                        .albumId("album-1")
                        .genreId("genre-1")
                        .build().handle());
        verify(songRepository).updateSong(any(Song.class));
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenTitleIsEmpty() {
        assertThrows(InvalidSongException.class, () ->
                UpdateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .id(UUID.randomUUID().toString())
                        .title("")
                        .durationSeconds(240)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenDurationIsZero() {
        assertThrows(InvalidSongException.class, () ->
                UpdateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .id(UUID.randomUUID().toString())
                        .title("New Title")
                        .durationSeconds(0)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenDurationIsNegative() {
        assertThrows(InvalidSongException.class, () ->
                UpdateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .id(UUID.randomUUID().toString())
                        .title("New Title")
                        .durationSeconds(-5)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenIdIsNotValidUUID() {
        assertThrows(InvalidSongException.class, () ->
                UpdateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .id("not-a-valid-uuid")
                        .title("New Title")
                        .durationSeconds(240)
                        .build().handle());
    }

    @Test
    void handle_shouldThrowSongNotFoundExceptionWhenSongDoesNotExist() {
        String id = UUID.randomUUID().toString();
        when(songRepository.getSongById(id)).thenReturn(Optional.empty());

        assertThrows(SongNotFoundException.class, () ->
                UpdateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .id(id)
                        .title("New Title")
                        .durationSeconds(240)
                        .build().handle());
        verify(songRepository, never()).updateSong(any());
    }
}
