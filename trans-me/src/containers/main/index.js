import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  resetState,
  selectGlobal,
  setAudioFiles,
  setBlocks,
} from "../../slices/globalSlice";
import { selectSession } from "../../slices/sessionSlice";

import { AccountAPI, AudioFileAPI, BlockAPI } from "../../api";
import Board from "./board";
import Term from "./term";
import AudioLibrary from "./audioLibrary";

import {
  Alert,
  AppBar,
  Avatar,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid,
  IconButton,
  Snackbar,
  Toolbar,
  Tooltip,
  Typography,
} from "@mui/material";
import moment from "moment";
import TurndownService from "turndown";

export default function Main() {
  const dispatch = useDispatch();
  const { username } = useSelector(selectSession);
  const { blocks, audioFiles } = useSelector(selectGlobal);

  const [alert, setAlert] = useState({});
  const [dialog, setDialog] = useState({ open: false, title: "", content: "" });
  const [welcome, setWelcome] = useState(false);

  const saveAccountData = async () => {
    const audioFilesId = audioFiles.map((audioFile) => audioFile.id);
    const blocksId = blocks.map((block) => block.id);
    const response = await AccountAPI.putAccount({
      username,
      blocksId,
      audioFilesId,
    });
    if (response?.status === 200) {
      setAlert({
        open: true,
        severity: "success",
        msg: "Account data saved.",
      });
    } else {
      setAlert({
        open: true,
        severity: "error",
        msg: "Failed to save account data.",
      });
    }
  };

  const handleExport = () => {
    const stringContent = blocks
      .filter((block) => !block.hidden)
      .map((block) => block.content)
      .join("");
    if (stringContent === "") {
      setAlert({
        open: true,
        severity: "warning",
        msg: "No content or no unhidden block to export.",
      });
    } else {
      const turndownService = new TurndownService();
      const markdown = turndownService.turndown(stringContent);
      const element = window.document.createElement("a");
      element.href = window.URL.createObjectURL(
        new Blob([markdown], { type: "text/plain" })
      );
      element.download =
        "transme-export_" + moment().format("YYYY-MM-DD_HH-mm-ss") + ".md";
      document.body.appendChild(element);
      element.click();
      document.body.removeChild(element);
    }
  };

  const handleNewProject = () => {
    setDialog({
      open: true,
      title: "New Project",
      content:
        "All the blocks and audio files from current project will be deleted and cannot be recovered. Are you sure to create a new project?",
      onConfirm: async () => {
        dispatch(resetState());
        await BlockAPI.deleteBlocks(blocks.map((block) => block.id));
        const response = await AccountAPI.putAccount({
          username,
          blocksId: [],
          audioFilesId: [],
        });
        if (response?.status === 200) {
          setAlert({
            open: true,
            severity: "success",
            msg: "New project created.",
          });
        } else {
          setAlert({
            open: true,
            severity: "error",
            msg: "Failed to create new project.",
          });
        }
        setDialog({ ...dialog, open: false });
      },
      onCancel: () => {
        setDialog({ ...dialog, open: false });
      },
    });
  };

  // TODO: initialize audioFiles from server
  useEffect(() => {
    function getAccountData() {
      BlockAPI.getBlocks(username).then((response) => {
        if (response?.status === 200) {
          dispatch(setBlocks(response.data.data));
        }
      });
      AudioFileAPI.getAudioFiles(username).then((response) => {
        if (response?.status === 200) {
          dispatch(setAudioFiles(response.data.data));
        }
      });
    }
    function handleWelcome() {
      setWelcome(true);
    }
    getAccountData();
    handleWelcome();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div>
      <Box sx={{ display: "flex" }}>
        <AppBar sx={{ width: "100%", pl: 2, pr: 2 }}>
          <Toolbar disableGutters>
            <Typography
              variant="h4"
              noWrap
              sx={{
                mr: 2,
                display: "flex",
                flexGrow: 1,
                fontFamily: "Playfair Display SC",
                fontWeight: 500,
                color: "inherit",
                textDecoration: "none",
              }}
            >
              TransMe
            </Typography>
            <Box sx={{ display: "flex", mr: 2 }}>
              <Button
                key={"save-button"}
                sx={{ my: 2, mr: 1, display: "block" }}
                onClick={saveAccountData}
                color="secondary"
                variant="contained"
              >
                Save
              </Button>
              <Button
                key={"export-button"}
                sx={{ my: 2, color: "white", display: "block" }}
                onClick={handleExport}
              >
                Export
              </Button>
              <Button
                key={"new-project-button"}
                sx={{ my: 2, color: "white", display: "block" }}
                onClick={handleNewProject}
              >
                New Project
              </Button>
            </Box>

            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title={"Hello, " + username}>
                <IconButton sx={{ p: 0 }}>
                  <Avatar alt={username}>{username[0].toUpperCase()}</Avatar>
                </IconButton>
              </Tooltip>
            </Box>
          </Toolbar>
        </AppBar>
        <Box sx={{ width: "100%" }}>
          <Toolbar />
          <Grid container spacing={2} sx={{ pt: 3, pr: 2, pl: 2 }}>
            <Grid item xs>
              <AudioLibrary />
            </Grid>
            <Grid item xs={7}>
              <Board />
            </Grid>
            <Grid item xs>
              <Term />
            </Grid>
          </Grid>
        </Box>
      </Box>
      <Dialog open={dialog?.open} onClose={dialog?.onCancel}>
        <DialogTitle>{dialog?.title}</DialogTitle>
        <DialogContent>
          <DialogContentText>{dialog?.content}</DialogContentText>
        </DialogContent>
        <DialogActions sx={{ pr: 3, pb: 2 }}>
          <Button onClick={dialog?.onCancel}>Cancel</Button>
          <Button
            onClick={dialog?.onConfirm}
            color="secondary"
            variant="contained"
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog open={welcome} onClose={() => setWelcome(false)}>
        <DialogTitle>Welcome to TransMe</DialogTitle>
        <DialogContent>
          <Typography variant="body1" component="div">
            This is a multi-functional tool for transcription. We support:
            <ul>
              <li>audio-to-text conversion</li>
              <li>audio recording with speach recognition</li>
              <li>summary generating</li>
              <li>term detection</li>
              <li>text editing</li>
              <li>translation</li>
            </ul>
          </Typography>
          <Typography variant="body1">
            <i>
              Chrome is recommended for the best experience with above features.
            </i>
          </Typography>
          <br />
          <Typography variant="body1">
            You can use the buttons on the top right corner to save your
            project, export the transcription, or create a new project.{" "}
            <b>
              Make sure to save your project before you leave the page to avoid
              losing your work.
            </b>{" "}
            Enjoy!
          </Typography>
        </DialogContent>
        <DialogActions sx={{ pr: 3, pb: 3 }}>
          <Button onClick={() => setWelcome(false)} variant="contained">
            Got it!
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
    </div>
  );
}
