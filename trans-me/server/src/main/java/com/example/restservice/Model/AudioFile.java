package com.example.restservice.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document("audiofiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AudioFile {
    @Id
    private String id;

    @NonNull
    private String name;
    @NonNull
    private String url;
    @NonNull
    private String driveId;
    @NonNull
    private String format;
    @NonNull
    private int sampleRate;
    @NonNull
    private String language;
}
