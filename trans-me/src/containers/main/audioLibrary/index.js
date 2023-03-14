import React, { useState } from "react";
import {
  Alert,
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControl,
  Grid,
  IconButton,
  InputLabel,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  ListSubheader,
  Menu,
  MenuItem,
  Paper,
  Select,
  Snackbar,
  Tooltip,
  Typography,
} from "@mui/material";
import UploadIcon from "@mui/icons-material/Upload";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { useDispatch, useSelector } from "react-redux";
import {
  addBlock,
  selectGlobal,
  setAudioFiles,
} from "../../../slices/globalSlice";
import { selectSession } from "../../../slices/sessionSlice";
import { AudioFileAPI, BlockAPI, TranscriptionAPI } from "../../../api";

export default function AudioLibrary() {
  const dispatch = useDispatch();
  const { username } = useSelector(selectSession);
  const { audioFiles } = useSelector(selectGlobal);
  const [alert, setAlert] = useState({});
  const [selectedIndex, setSelectedIndex] = useState();
  const [anchorEl, setAnchorEl] = useState(null);
  const [isMenuOpened, setIsMenuOpened] = useState(false);
  const [isUploadDialogOpened, setIsUploadDialogOpened] = useState(false);
  const [uploadFile, setUploadFile] = useState(null);
  const [language, setLanguage] = useState("en-US");
  const [currentPlayingAudioFile, setCurrentPlayingAudioFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleMenuClick = (event, index) => {
    setAnchorEl(event.currentTarget);
    setSelectedIndex(index);
    setIsMenuOpened(true);
  };

  const handlePlayFile = () => {
    setCurrentPlayingAudioFile(audioFiles[selectedIndex]);
    setIsMenuOpened(false);
  };

  const handleTranscribeFile = () => {
    setLoading(true);
    setIsMenuOpened(false);
    TranscriptionAPI.postTranscription(
      username,
      audioFiles[selectedIndex].id
    ).then((response) => {
      if (response?.status === 200) {
        BlockAPI.postBlock({
          content: response.data.data,
          hidden: false,
          username: username,
        }).then((response) => {
          if (response?.status === 200) {
            dispatch(addBlock(response.data.data));
            setAlert({
              open: true,
              severity: "success",
              msg: "Transcription ",
            });
          } else {
            setAlert({
              open: true,
              severity: "error",
              msg: "Transcription failed.",
            });
          }
        });
      } else {
        setAlert({
          open: true,
          severity: "error",
          msg: "Transcription failed.",
        });
      }
      setLoading(false);
    });
  };

  const handleDownloadFile = () => {
    const link = document.createElement("a");
    link.href = audioFiles[selectedIndex].url;
    link.setAttribute("download", audioFiles[selectedIndex].name);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    setIsMenuOpened(false);
  };

  const handleDeleteFile = () => {
    AudioFileAPI.deleteAudioFiles(audioFiles[selectedIndex].id, username)
      .then((response) => {
        if (response?.status === 200) {
          dispatch(
            setAudioFiles(
              audioFiles.filter((_, index) => index !== selectedIndex)
            )
          );
          setAlert({
            open: true,
            severity: "success",
            msg: "Audio file deleted.",
          });
        } else {
          setAlert({
            open: true,
            severity: "error",
            msg: "Failed to delete audio file.",
          });
        }
      })
      .catch((error) => {
        setAlert({
          open: true,
          severity: "error",
          msg: "Failed to delete audio file.",
        });
      });
    setIsMenuOpened(false);
  };

  const handleOpenUpload = () => {
    setIsUploadDialogOpened(true);
  };

  const handleCloseUpload = () => {
    setIsUploadDialogOpened(false);
    setUploadFile(null);
  };

  const handleFileChange = (e) => {
    setUploadFile(e.target.files[0]);
  };

  const handleLanguageChange = (e) => {
    setLanguage(e.target.value);
  };

  const handleUpload = () => {
    if (uploadFile) {
      const formData = new FormData();
      formData.append("username", username);
      formData.append("name", uploadFile.name);
      formData.append("format", "wav");
      formData.append("file", uploadFile);
      formData.append("language", language);
      formData.append(
        "sampleRate",
        new Blob(["44100"], { type: "application/json" })
      );
      AudioFileAPI.postAudioFile(formData).then((response) => {
        if (response?.status === 200) {
          dispatch(setAudioFiles([...audioFiles, response.data.data]));
          setAlert({
            open: true,
            severity: "success",
            msg: "Audio file uploaded.",
          });
        } else {
          setAlert({
            open: true,
            severity: "error",
            msg: "Failed to upload audio file.",
          });
        }
      });
      handleCloseUpload();
    } else {
      setAlert({
        open: true,
        severity: "error",
        msg: "Please select a file to upload.",
      });
    }
  };
  return (
    <>
      <Paper variant="outlined" sx={{ height: "85vh", overflow: "scroll" }}>
        <ListSubheader
          sx={{
            pt: 2,
            pb: 1,
            mb: 1,
            zIndex: 950,
            backgroundColor: "rgba(255, 255, 255, 1)",
            borderBottom: "1px solid rgba(0, 0, 0, 0.12)",
          }}
        >
          <Grid
            container
            direction="row"
            justifyContent="space-between"
            alignItems="stretch"
          >
            <Grid item>
              <Typography
                gutterBottom
                variant="h6"
                sx={{
                  fontFamily: "Playfair Display",
                  color: "rgba(34, 34, 34, 1)",
                  fontWeight: 700,
                  mt: 1,
                }}
              >
                Audio Library
              </Typography>
            </Grid>
            <Grid item>
              <Tooltip title="Upload">
                <IconButton size="small" onClick={handleOpenUpload}>
                  <UploadIcon />
                </IconButton>
              </Tooltip>
            </Grid>
          </Grid>
          {currentPlayingAudioFile && (
            <audio
              src={currentPlayingAudioFile.url.slice(0, -16)}
              controls
              autoPlay
              style={{ width: "280px" }}
            />
          )}
        </ListSubheader>

        <List>
          {audioFiles.map((audioFile, index) => (
            <ListItemButton
              key={audioFile.id}
              selected={selectedIndex === index}
              onClick={() => setSelectedIndex(index)}
            >
              <ListItem disablePadding>
                <ListItemText
                  primary={audioFile.name}
                  secondary={audioFile.language}
                  sx={{
                    width: "150px",
                    textOverflow: "ellipsis",
                    overflow: "hidden",
                  }}
                />
                {loading && selectedIndex === index ? (
                  <CircularProgress size={20} />
                ) : (
                  <IconButton
                    edge="end"
                    aria-label="more"
                    onClick={handleMenuClick}
                    disabled={loading}
                    aria-controls={isMenuOpened ? "menu" : undefined}
                    aria-haspopup="true"
                    aria-expanded={isMenuOpened ? "true" : undefined}
                  >
                    <MoreHorizIcon />
                  </IconButton>
                )}
              </ListItem>
            </ListItemButton>
          ))}
        </List>

        <Menu
          id="audio-menu"
          anchorEl={anchorEl}
          open={isMenuOpened}
          onClose={() => setIsMenuOpened(false)}
          MenuListProps={{
            "aria-labelledby": "basic-button",
          }}
        >
          <MenuItem onClick={handlePlayFile}>Play</MenuItem>
          <MenuItem onClick={handleTranscribeFile}>Transcibe</MenuItem>
          <MenuItem onClick={handleDownloadFile}>Download</MenuItem>
          <MenuItem onClick={handleDeleteFile}>Delete</MenuItem>
        </Menu>
      </Paper>
      <Dialog open={isUploadDialogOpened}>
        <DialogTitle>Upload Audio</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ mb: 4 }}>
            Upload an audio file to the audio library. Supported file type is
            WAV. Please select the language of the audio file for better
            recognition.
            <br />
            <br />
            Audio File:{" "}
            <input type="file" accept="audio/wav" onChange={handleFileChange} />
          </DialogContentText>
          <FormControl fullWidth>
            <InputLabel id="language-select-label">Language</InputLabel>
            <Select
              labelId="language-select-label"
              id="language-select"
              value={language}
              label="Language"
              onChange={handleLanguageChange}
            >
              <MenuItem value={"zh-TW"}>Chinese (Traditional)</MenuItem>
              <MenuItem value={"en-US"}>English</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions sx={{ pr: 3, pb: 3 }}>
          <Button onClick={handleCloseUpload}>Cancel</Button>
          <Button onClick={handleUpload} variant="contained">
            Upload
          </Button>
        </DialogActions>
      </Dialog>
      <Snackbar
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
        open={alert?.open}
        autoHideDuration={5000}
        onClose={() => setAlert({ ...alert, open: false })}
      >
        <Alert variant="filled" severity={alert?.severity}>
          {alert?.msg}
        </Alert>
      </Snackbar>
    </>
  );
}
