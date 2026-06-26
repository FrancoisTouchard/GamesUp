package com.gamesUP.gamesUP.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameFeatureDTO {

    private UUID id;

    private String name;

    @JsonProperty("genre_names")
    private List<String> genreNames;

    @JsonProperty("category_names")
    private List<String> categoryNames;

    @JsonProperty("author_names")
    private List<String> authorNames;

    @JsonProperty("publisher_name")
    private String publisherName;
}
