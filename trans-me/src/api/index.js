import axios from "axios";

const errorHandler = (error) => {
  if (error.response.status === 403) window.location.replace("/");
  // cannot connect to server
};

export const AccountAPI = {
  getAccount: (username, password) =>
    axios
      .get("/api/account", { params: { username, password } })
      .catch(errorHandler),
  postAccount: (username, password) =>
    axios
      .post("/api/account", { data: { username, password } })
      .catch(errorHandler),
  putAccount: (newAccountData) =>
    axios.put("/api/account", { data: newAccountData }).catch(errorHandler),
};

export const BlockAPI = {
  getBlocks: (username) =>
    axios.get("/api/block", { params: { username } }).catch(errorHandler),
  postBlock: (block) =>
    axios.post("/api/block", { data: { block } }).catch(errorHandler),
  putBlocks: (blocks) =>
    axios.put("/api/block", { data: { blocks } }).catch(errorHandler),
  deleteBlocks: (blocksId) =>
    fetch("/api/block", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ data: { blocksId } }),
    }).catch(errorHandler),
};

export const TermAPI = {
  postTerms: (content) =>
    axios.post("/api/term", { data: { content } }).catch(errorHandler),
};

export const TranscriptionAPI = {
  postTranscription: (username, audioFileId) =>
    axios
      .post("/api/transcription", { data: { username, audioFileId } })
      .catch(errorHandler),
};

export const TranslationAPI = {
  postTranslation: (content, language) =>
    axios
      .post("/api/translate", { data: { content, language } })
      .catch(errorHandler),
};

export const SummaryAPI = {
  postSummary: (content) =>
    axios.post("/api/summary", { data: { content } }).catch(errorHandler),
};

export const AudioFileAPI = {
  getAudioFiles: (username) =>
    axios.get("/api/audioFile", { params: { username } }).catch(errorHandler),
  postAudioFile: (formData) =>
    axios({
      method: "post",
      url: "/api/audioFile",
      data: formData,
      headers: { "Content-Type": "multipart/form-data" },
    }).catch(errorHandler),
  putAudioFile: (id, name) =>
    axios.put("/api/audioFile", { data: { id, name } }).catch(errorHandler),
  deleteAudioFiles: (id, username) =>
    fetch("/api/audioFile", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ data: { id, username } }),
    }).catch(errorHandler),
};
