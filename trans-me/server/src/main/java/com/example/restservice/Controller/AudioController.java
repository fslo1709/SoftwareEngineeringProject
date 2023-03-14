package com.example.restservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.restservice.Response.CommonResponse;
import com.example.restservice.Response.Msg;
import com.example.restservice.Service.AudioFilesService;
import com.example.restservice.Service.Payload.Payload;
import com.example.restservice.Model.AudioFile;
import com.example.restservice.Request.audioFile.DeleteAudioFileRequest;
import com.example.restservice.Request.audioFile.PutAudioFileRequest;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Audio File Controller")
@RestController
@RequestMapping("/audioFile")
public class AudioController {
    @Autowired
    AudioFilesService audioFilesService;

    @GetMapping("")
    public CommonResponse<List<AudioFile>> getAudioFilesByUsername(@RequestParam("username") String username) {
        Payload <Msg, List<AudioFile>> result = audioFilesService.getAudioFiles(username);

        return new CommonResponse<List<AudioFile>>(
            result.getMsg(),
            result.getData()
        );
    }

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE,
                                    MediaType.MULTIPART_FORM_DATA_VALUE })
    public CommonResponse <AudioFile> postAudioFile(@RequestPart("username") String username,
                                                    @RequestPart("name") String name,
                                                    @RequestPart("format") String format,
                                                    @RequestPart("file") MultipartFile file,
                                                    @RequestPart("sampleRate") int sampleRate,
                                                    @RequestPart("language") String language) {
        Payload <Msg, AudioFile> result = audioFilesService.postAudioFile(username, name, format, file, sampleRate, language);

        return new CommonResponse <AudioFile>(
            result.getMsg(),
            result.getData()
        );
    }

    @PutMapping("")
    public CommonResponse <AudioFile> putAudioFile(@RequestBody PutAudioFileRequest req) {
        String id = req.getData().getId();
        String name = req.getData().getName();

        Payload <Msg, AudioFile> result = audioFilesService.updateAudioFile(id, name);

        return new CommonResponse <AudioFile>(
            result.getMsg(),
            result.getData()
        );
    }

    @DeleteMapping("")
    public CommonResponse <String> deleteAudioFile(@RequestBody DeleteAudioFileRequest req) {
        String username = req.getData().getUsername();
        String id = req.getData().getId();

        Payload <Msg, String> result = audioFilesService.deleteAudioFile(username, id);

        return new CommonResponse <String>(
            result.getMsg(),
            result.getData()
        );
    }
}
