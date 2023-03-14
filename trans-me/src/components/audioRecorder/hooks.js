import { useState, useCallback } from "react";

const useAudioRecorder = () => {
  const [isRecording, setIsRecording] = useState(false);
  const [isPaused, setIsPaused] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const [mediaRecorder, setMediaRecorder] = useState();
  const [timerInterval, setTimerInterval] = useState();
  const [recordingBlob, setRecordingBlob] = useState();

  const _startTimer = () => {
    const interval = setInterval(() => {
      setRecordingTime((time) => time + 1);
    }, 1000);
    setTimerInterval(interval);
  };

  const _stopTimer = () => {
    timerInterval != null && clearInterval(timerInterval);
    setTimerInterval(undefined);
  };

  /**
   * Calling this method would result in the recording to start. Sets `isRecording` to true
   */
  const startRecording = useCallback(() => {
    if (timerInterval != null) return;

    navigator.mediaDevices
      .getUserMedia({ audio: true })
      .then((stream) => {
        setIsRecording(true);
        const recorder = new MediaRecorder(stream);
        setMediaRecorder(recorder);
        recorder.start();
        _startTimer();

        recorder.addEventListener("dataavailable", (event) => {
          setRecordingBlob(event.data);
          recorder.stream.getTracks().forEach((t) => t.stop());
          setMediaRecorder(null);
        });
      })
      .catch((err) => console.log(err));
  }, [timerInterval]);

  /**
   * Calling this method results in a recording in progress being stopped and the resulting audio being present in `recordingBlob`. Sets `isRecording` to false
   */
  const stopRecording = () => {
    mediaRecorder?.stop();
    _stopTimer();
    setRecordingTime(0);
    setIsRecording(false);
    setIsPaused(false);
    setRecordingBlob(null);
  };

  /**
   * Calling this method would pause the recording if it is currently running or resume if it is paused. Toggles the value `isPaused`
   */
  const togglePauseResume = () => {
    if (isPaused) {
      setIsPaused(false);
      mediaRecorder?.resume();
      _startTimer();
    } else {
      setIsPaused(true);
      _stopTimer();
      mediaRecorder?.pause();
    }
  };

  return {
    startRecording,
    stopRecording,
    togglePauseResume,
    recordingBlob,
    isRecording,
    isPaused,
    recordingTime,
  };
};

export default useAudioRecorder;
