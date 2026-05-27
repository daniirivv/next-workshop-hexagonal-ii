package com.music.streaming.user.infrastructure.rest.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@Getter
@ToString
public class PostStarredSongRequestDTO {
    final String songId;
}
