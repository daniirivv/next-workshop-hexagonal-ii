package com.music.streaming.catalog.infrastructure.rest;

import com.music.streaming.catalog.application.command.CreateSongCommand;
import com.music.streaming.catalog.application.command.DeleteSongCommand;
import com.music.streaming.catalog.application.command.UpdateSongCommand;
import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.application.query.GetAllSongsQuery;
import com.music.streaming.catalog.application.query.GetSongByIdQuery;
import com.music.streaming.catalog.domain.DuplicatedSongException;
import com.music.streaming.catalog.domain.InvalidSongException;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.domain.SongDomainService;
import com.music.streaming.catalog.domain.SongNotFoundException;
import com.music.streaming.catalog.infrastructure.rest.dto.request.PatchSongRequestDTO;
import com.music.streaming.catalog.infrastructure.rest.dto.request.PostSongRequestDTO;
import com.music.streaming.catalog.infrastructure.rest.dto.response.GetSongResponseDTO;
import com.music.streaming.catalog.infrastructure.rest.mapper.SongFacadeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    final SongFacadeMapper songFacadeMapper;
    final SongRepositoryPort songRepository;
    final SongDomainService songDomainService;

    @GetMapping
    public ResponseEntity<List<GetSongResponseDTO>> getAllSongs() {
        List<Song> songs = GetAllSongsQuery.builder().songRepository(songRepository).build().execute();
        if (songs.isEmpty()) return ResponseEntity.noContent().build();
        List<GetSongResponseDTO> songsResponse = songs.stream().map(songFacadeMapper::fromDomain).toList();
        return ResponseEntity.ok(songsResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createSong(@RequestBody PostSongRequestDTO songDto) {
        try {
            String id = CreateSongCommand.builder()
                    .songRepository(songRepository)
                    .songDomainService(songDomainService)
                    .title(songDto.getTitle())
                    .durationSeconds(songDto.getDurationSeconds())
                    .artistId(songDto.getArtistId())
                    .albumId(songDto.getAlbumId())
                    .genreId(songDto.getGenreId())
                    .build().handle();
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).build();
        } catch (DuplicatedSongException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InvalidSongException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDTO> getSongById(@PathVariable String id) {
        Optional<Song> song = GetSongByIdQuery.builder().songRepository(songRepository).id(id).build().execute();
        return song.map(value -> ResponseEntity.ok(songFacadeMapper.fromDomain(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable String id, @RequestBody PatchSongRequestDTO songDto) {
        try {
            UpdateSongCommand.builder()
                    .songRepository(songRepository)
                    .songDomainService(songDomainService)
                    .id(id)
                    .title(songDto.getTitle())
                    .durationSeconds(songDto.getDurationSeconds())
                    .artistId(songDto.getArtistId())
                    .albumId(songDto.getAlbumId())
                    .genreId(songDto.getGenreId())
                    .build().handle();
        } catch (SongNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidSongException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) {
        try {
            DeleteSongCommand.builder().songRepository(songRepository).id(id).build().handle();
        } catch (InvalidSongException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (SongNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
