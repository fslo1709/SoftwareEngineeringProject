import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  insertBlock,
  selectGlobal,
  setAudioFiles,
} from "../../../../../slices/globalSlice";
import { selectSession } from "../../../../../slices/sessionSlice";

import { AudioFileAPI, BlockAPI } from "../../../../../api";

import {
  Alert,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Snackbar,
  TextField,
} from "@mui/material";

import SpeechRecognition, {
  useSpeechRecognition,
} from "react-speech-recognition";
import useAudioRecorder from "../../../../../components/audioRecorder/hooks";
import AudioRecorder from "../../../../../components/audioRecorder";
import moment from "moment";

export default function RecordingOperator({ index, open, setOpen }) {
  const dispatch = useDispatch();
  const { username } = useSelector(selectSession);
  const { audioFiles } = useSelector(selectGlobal);

  const audioBoxRef = useRef();
  const [isListening, setIsListening] = useState(false);
  const [alert, setAlert] = useState({});

  const { transcript, resetTranscript } = useSpeechRecognition();

  const recorderControls = useAudioRecorder();
  const controls = {
    ...recorderControls,
    startRecording: () => {
      recorderControls.startRecording();
      handleListing();
    },
    stopRecording: () => {
      recorderControls.stopRecording();
      handleStopListening();
    },
    togglePauseResume: () => {
      if (isListening) {
        handleStopListening();
      } else {
        handleListing();
      }
      recorderControls.togglePauseResume();
    },
  };

  const addAudioElement = (blob) => {
    const url = URL.createObjectURL(blob);
    const audio = document.createElement("audio");
    audio.src = url;
    audio.controls = true;
    audioBoxRef.current.appendChild(audio);
    // upload audio file to server
    const formData = new FormData();
    formData.append("file", blob);
    formData.append("username", username);
    formData.append("name", "Record_" + moment().format("YYMMDDHHmmss"));
    formData.append("format", "webm");
    formData.append("language", "zh-TW");
    formData.append(
      "sampleRate",
      new Blob(["48000"], { type: "application/json" })
    );
    AudioFileAPI.postAudioFile(formData).then((response) => {
      if (response.status === 200) {
        dispatch(setAudioFiles([...audioFiles, response.data.data]));
      } else {
        setAlert({
          open: true,
          msg: "Upload audio file failed",
          severity: "error",
        });
      }
    });
  };

  const handleListing = () => {
    setIsListening(true);
    SpeechRecognition.startListening({
      continuous: true,
    });
  };
  const handleStopListening = () => {
    setIsListening(false);
    SpeechRecognition.stopListening();
  };

  const handleCloseRecord = () => {
    controls.stopRecording();
    audioBoxRef.current.replaceChildren();
    setOpen(false);
  };

  const handleFinishRecord = async () => {
    if (transcript !== "") {
      const response = await BlockAPI.postBlock({
        content: `<p>${transcript}</p>`,
        hidden: false,
        username,
      });
      dispatch(insertBlock({ index: index + 1, block: response.data.data }));
      handleCloseRecord();
    } else {
      setAlert({
        open: true,
        msg: "Please speak something before inserting",
        severity: "warning",
      });
    }
  };

  useEffect(() => {
    resetTranscript();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open]);

  return (
    <>
      <Dialog open={open} maxWidth="md" fullWidth>
        <DialogTitle>Recording Conversion</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please allow the access to your microphone and speak into it. Chrome
            Browser is recommended for this feature.
          </DialogContentText>
          <Box
            sx={{
              display: "grid",
              gridTemplateColumns: "auto auto",
              justifyContent: "center",
              p: 2,
              gap: 1,
              maxHeight: "30vh",
              overflowY: "scroll",
            }}
            ref={audioBoxRef}
          />
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              m: 2,
            }}
          >
            <AudioRecorder
              onRecordingComplete={(blob) => addAudioElement(blob)}
              recorderControls={controls}
            />
          </Box>
          <TextField
            id="recording-content"
            fullWidth
            multiline
            InputProps={{
              readOnly: true,
            }}
            value={transcript}
            error={!SpeechRecognition.browserSupportsSpeechRecognition()}
            helperText={
              !SpeechRecognition.browserSupportsSpeechRecognition() ??
              "Your browser does not support speech recognition"
            }
            rows={10}
          />
        </DialogContent>
        <DialogActions sx={{ pr: 3, pb: 2 }}>
          <Button onClick={handleCloseRecord}>Cancel</Button>
          <Button
            onClick={handleFinishRecord}
            color="secondary"
            variant="outlined"
          >
            Insert
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
