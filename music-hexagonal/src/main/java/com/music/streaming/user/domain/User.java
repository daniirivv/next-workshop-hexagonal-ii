package com.music.streaming.user.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {
    @Builder.Default
    final String id = UUID.randomUUID().toString();
    @With
    String username;
    @With
    String email;
    @Builder.Default
    @With
    List<String> starredSongIds = new ArrayList<>();
}
