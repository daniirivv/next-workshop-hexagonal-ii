package com.music.streaming.catalog.application.command;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.DuplicatedSongException;
import com.music.streaming.catalog.domain.InvalidSongException;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.domain.SongDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSongCommandTest {

    @Mock
    SongRepositoryPort songRepository;

    SongDomainService songDomainService = new SongDomainService();

    @Test
    void handle_shouldReturnIdWhenValid() throws InvalidSongException, DuplicatedSongException {
        when(songRepository.getSongByTitle("Bohemian Rhapsody")).thenReturn(Optional.empty());

        String id = CreateSongCommand.builder()
                .songRepository(songRepository)
                .songDomainService(songDomainService)
                .title("Bohemian Rhapsody")
                .durationSeconds(354)
                .artistId("artist-1")
                .albumId("album-1")
                .genreId("genre-1")
                .build().handle();

        assertNotNull(id);
        assertDoesNotThrow(() -> UUID.fromString(id));
        verify(songRepository).createSong(any(Song.class));
    }

    @Test
    void handle_shouldReturnIdWithoutOptionalFields() throws InvalidSongException, DuplicatedSongException {
        when(songRepository.getSongByTitle("Test Song")).thenReturn(Optional.empty());

        String id = CreateSongCommand.builder()
                .songRepository(songRepository)
                .songDomainService(songDomainService)
                .title("Test Song")
                .durationSeconds(200)
                .build().handle();

        assertNotNull(id);
        verify(songRepository).createSong(any(Song.class));
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenTitleIsEmpty() {
        assertThrows(InvalidSongException.class, () ->
                CreateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .title("")
                        .durationSeconds(354)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenTitleIsNull() {
        assertThrows(InvalidSongException.class, () ->
                CreateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .title(null)
                        .durationSeconds(354)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenDurationIsZero() {
        assertThrows(InvalidSongException.class, () ->
                CreateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .title("Test Song")
                        .durationSeconds(0)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenDurationIsNegative() {
        assertThrows(InvalidSongException.class, () ->
                CreateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .title("Test Song")
                        .durationSeconds(-10)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowInvalidSongExceptionWhenDurationIsNull() {
        assertThrows(InvalidSongException.class, () ->
                CreateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .title("Test Song")
                        .durationSeconds(null)
                        .build().handle());
        verifyNoInteractions(songRepository);
    }

    @Test
    void handle_shouldThrowDuplicatedSongExceptionWhenTitleAlreadyExists() {
        when(songRepository.getSongByTitle("Bohemian Rhapsody"))
                .thenReturn(Optional.of(Song.builder().title("Bohemian Rhapsody").durationSeconds(354).build()));

        assertThrows(DuplicatedSongException.class, () ->
                CreateSongCommand.builder()
                        .songRepository(songRepository)
                        .songDomainService(songDomainService)
                        .title("Bohemian Rhapsody")
                        .durationSeconds(354)
                        .build().handle());
        verify(songRepository, never()).createSong(any());
    }
}
