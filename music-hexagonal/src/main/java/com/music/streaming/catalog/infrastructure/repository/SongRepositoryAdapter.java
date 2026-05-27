package com.music.streaming.catalog.infrastructure.repository;

import com.music.streaming.catalog.application.port.SongRepositoryPort;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.infrastructure.repository.entity.SongEntity;
import com.music.streaming.catalog.infrastructure.repository.mapper.SongEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SongRepositoryAdapter implements SongRepositoryPort {
    final SongEntityMapper songEntityMapper;
    final SongJpaRepository songJpaRepository;

    @Override
    public List<Song> getSongs() {
        return songJpaRepository.findAll().stream().map(songEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Song> getSongById(String id) {
        return songJpaRepository.findById(id).map(songEntityMapper::toDomain);
    }

    @Override
    public Optional<Song> getSongByTitle(String title) {
        List<SongEntity> songs = songJpaRepository.findByTitle(title);
        return songs.isEmpty() ? Optional.empty() : songs.stream().findFirst().map(songEntityMapper::toDomain);
    }

    @Override
    public void deleteSong(String id) {
        songJpaRepository.deleteById(id);
    }

    @Override
    public void updateSong(Song song) {
        songJpaRepository.save(songEntityMapper.fromDomain(song));
    }

    @Override
    public void createSong(Song song) {
        songJpaRepository.save(songEntityMapper.fromDomain(song));
    }
}
