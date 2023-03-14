package com.example.restservice.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Service;

import com.example.restservice.Drive.DriveOperator;
import com.example.restservice.Model.Account;
import com.example.restservice.Model.AudioFile;
import com.example.restservice.Repository.AccountRepository;
import com.example.restservice.Repository.AudioFileRepository;
import com.example.restservice.Response.Msg;
import com.example.restservice.Service.Payload.Payload;

@Service
public class AudioFilesService {
    @Autowired
    AudioFileRepository audioFileRepository;

    @Autowired
    AccountRepository accountRepository;

    public Payload<Msg, List<AudioFile>> getAudioFiles(String username) {
        System.out.println("Getting audio files from a username");

        List<AudioFile> audioFiles = new ArrayList<AudioFile>();
        
        Account account = accountRepository.findByUsername(username);

        if (account != null) {
            List<String> audioFilesId = account.getAudioFilesId();
            if (audioFilesId == null) {
                System.out.println("No audio files found");
                return new Payload <Msg, List<AudioFile>> (
                        new Msg(
                            "warning",
                            "No audio files found"
                        ),
                        audioFiles
                    );
            }
            else {
                audioFilesId.forEach((id) -> {
                    Optional<AudioFile> audioFile = audioFileRepository.findById(id);
                    if (audioFile.isPresent()) {
                        audioFiles.add(audioFile.get());
                    }
                });

                if (audioFiles.isEmpty()) {
                    System.out.println("No audio files found");
                    return new Payload <Msg, List<AudioFile>> (
                            new Msg(
                                "warning",
                                "No audio files found"
                            ),
                            audioFiles
                        );
                }
                else if (audioFiles.size() != audioFilesId.size()) {
                    System.out.println("Some of the Audio Files were not found");
                    return new Payload <Msg, List<AudioFile>> (
                            new Msg(
                                "warning",
                                "Some of the Audio Files were not found"
                            ),
                            audioFiles
                        );
                }
                else {
                    System.out.println("Success, return all Audio Files data");
                    return new Payload <Msg, List<AudioFile>> (
                            new Msg(
                                "success",
                                "return all Audio Files data"
                                ),
                            audioFiles
                        );
                }
            }
        }
        else {
            System.out.println("Username not found");
            return new Payload <Msg, List<AudioFile>> (
                    new Msg(
                        "error",
                        "Username not found"
                    ),
                    audioFiles
                );
        }
    }

    public Payload<Msg, AudioFile> postAudioFile(String username, String name, String format, MultipartFile file, int sampleRate, String language) {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            System.out.println("Account not found");
            return new Payload<Msg,AudioFile>(
                new Msg(
                    "error", 
                    "Account not found"
                    ),
                null
            );
        }

        String subfolderId = account.getDriveId();
        System.out.println("Driveid of the account is: " + subfolderId);
        List<String> responseGoogleDrive = new ArrayList<String>();

        System.out.println("Uploading audio file to google drive");

        try {
            responseGoogleDrive = DriveOperator.uploadFile(file, subfolderId, name, format);
        }
        catch (Exception e) {
            System.out.println("Error uploading to google drive, " + e);
            return new Payload<Msg,AudioFile>(
                new Msg(
                    "error",
                    "Error uploading to google drive" + e.getMessage()
                ), 
            null
            );
        }

        String driveId = responseGoogleDrive.get(0);
        String url = responseGoogleDrive.get(1);
        AudioFile newAudioFile = new AudioFile(null, name, url, driveId, format, sampleRate, language);

        AudioFile savedAudioFile = audioFileRepository.save(newAudioFile);

        if (savedAudioFile != null) {
            System.out.println("Success, return audio file data");
            System.out.println("Audio file url : " + savedAudioFile.getUrl() + "\nAudio file id: " + savedAudioFile.getDriveId());

            // Update the list of audio files of the given user
            List<String> audioFilesId = account.getAudioFilesId();
            if (audioFilesId == null) {
                audioFilesId = new ArrayList<String>();
            }
            audioFilesId.add(savedAudioFile.getId());
            account.setAudioFilesId(audioFilesId);
            accountRepository.save(account);

            return new Payload <Msg, AudioFile> (
                    new Msg(
                        "success",
                        "return audio file data"
                        ),
                    savedAudioFile
                );
        }
        else {
            System.out.println("Error, couldn't add audio file to mongodb");
            return new Payload <Msg, AudioFile> (
                    new Msg(
                        "error",
                        "could not add audio file to db"
                        ),
                    savedAudioFile
                );
        }
    }

    public Payload<Msg, AudioFile> updateAudioFile(String id, String name) {
        Optional<AudioFile> savedAudioFile = audioFileRepository.findById(id);
        
        if (savedAudioFile.isPresent()) {
            AudioFile updatedAudioFile = savedAudioFile.get();
            updatedAudioFile.setName(name);
            AudioFile responseAudioFile = audioFileRepository.save(updatedAudioFile);
            if (responseAudioFile != null) {
                System.out.println("Successfully renamed audio file to: " + responseAudioFile.getName());
                return new Payload <Msg, AudioFile> (
                    new Msg(
                        "success",
                        "Successfully renamed audio file"
                    ),
                    responseAudioFile
                );
            }
            else {
                System.out.println("Error, couldn't add audio file");
                return new Payload <Msg, AudioFile> (
                    new Msg(
                        "error",
                        "Couldn't add audio file"
                    ),
                    responseAudioFile
                );
            }
        }
        else {
            System.out.println("Error, couldn't find audio file");
            return new Payload <Msg, AudioFile> (
                    new Msg(
                        "error",
                        "could not find audio file"
                        ),
                    new AudioFile()
                );
        }
    }

    public Payload<Msg, String> deleteAudioFile(String username, String id) {
        Account account = accountRepository.findByUsername(username);
        
        if (account == null) {
            System.out.println("Account not found");
            return new Payload<Msg, String>(
                new Msg(
                    "error", 
                    "Account not found"
                    ),
                ""
            );
        }

        List<String> audioFilesIds = account.getAudioFilesId();

        if (!audioFilesIds.contains(id)) {
            System.out.println("Audio File doesn't belong to this user");
            return new Payload<Msg, String>(
                new Msg(
                    "error", 
                    "Audio File doesn't belong to this user"
                    ),
                ""
            );
        }

        // Remove from account list of audio files
        audioFilesIds.remove(id);
        account.setAudioFilesId(audioFilesIds);
        Account updatedAccount = accountRepository.save(account);
        System.out.println("Successfully updated account's audio files list: " + updatedAccount.getAudioFilesId());

        Optional<AudioFile> savedAudioFile = audioFileRepository.findById(id);
        if (!savedAudioFile.isPresent()) {
            System.out.println("Error, couldn't find audio file in mongoDB");
            return new Payload <Msg, String> (
                    new Msg(
                        "error",
                        "could not find audio file"
                        ),
                    ""
                );
        }
        
        // Remove from google Drive
        String googleDriveId = savedAudioFile.get().getDriveId();
        try {
            DriveOperator.deleteFile(googleDriveId);
        }
        catch (Exception e) {
            // Might have deleted by mistake, but still will delete from MongoDB
            System.out.println("Error deleting from google drive");
        }
        
        // Remove from MongoDB    
        AudioFile audioFileToBeDeleted = savedAudioFile.get();
        audioFileRepository.delete(audioFileToBeDeleted);

        System.out.println("Successfully deleted audio file");
        return new Payload<Msg,String>(
            new Msg(
                "success",
                "Successfully deleted audio file"
            ), 
            ""
            );
    }
}
