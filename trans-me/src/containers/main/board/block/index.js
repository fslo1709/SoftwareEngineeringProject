import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  deleteBlock,
  insertBlock,
  moveBlockDown,
  moveBlockUp,
  toggleBlockVisibility,
  updateBlockContent,
} from "../../../../slices/globalSlice";
import RichEditor from "./richEditor";
import {
  Alert,
  Box,
  Button,
  ButtonGroup,
  CardContent,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControl,
  IconButton,
  InputLabel,
  Menu,
  MenuItem,
  Paper,
  Select,
  Snackbar,
  Tooltip,
} from "@mui/material";

import NorthRoundedIcon from "@mui/icons-material/NorthRounded";
import SouthRoundedIcon from "@mui/icons-material/SouthRounded";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import DeleteRoundedIcon from "@mui/icons-material/DeleteRounded";
import ContentCopyRoundedIcon from "@mui/icons-material/ContentCopyRounded";
import VisibilityRoundedIcon from "@mui/icons-material/VisibilityRounded";
import VisibilityOffRoundedIcon from "@mui/icons-material/VisibilityOffRounded";
import MoreVertRoundedIcon from "@mui/icons-material/MoreVertRounded";
import { BlockAPI, SummaryAPI, TranslationAPI } from "../../../../api";
import RecordingOperator from "./recordingOperator";
import { selectSession } from "../../../../slices/sessionSlice";

export default function Block({ index, id, content, hidden }) {
  const dispatch = useDispatch();
  const { username } = useSelector(selectSession);

  const [alert, setAlert] = useState({});
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [isMenuOpened, setIsMenuOpened] = useState(false);
  const [isRecordOpened, setIsRecordOpened] = useState(false);
  const [isTranslateOpened, setIsTranslateOpened] = useState(false);
  const [language, setLanguage] = useState("en");
  const [loading, setLoading] = useState(false);

  const BLOCK_TOOLBAR = [
    {
      title: "Move Up",
      icon: <NorthRoundedIcon />,
      onClick: () => {
        dispatch(moveBlockUp({ id }));
      },
    },
    {
      title: "Move Down",
      icon: <SouthRoundedIcon />,
      onClick: () => {
        dispatch(moveBlockDown({ id }));
      },
    },
    {
      title: "Add Block",
      icon: <AddRoundedIcon />,
      onClick: async () => {
        const response = await BlockAPI.postBlock({
          content: "",
          hidden: false,
          username,
        });
        dispatch(insertBlock({ index: index + 1, block: response.data.data }));
      },
    },
    {
      title: "Delete Block",
      icon: <DeleteRoundedIcon />,
      onClick: async () => {
        await BlockAPI.deleteBlocks([id]);
        dispatch(deleteBlock({ id }));
      },
    },
    {
      title: "Duplicate Block",
      icon: <ContentCopyRoundedIcon />,
      onClick: async () => {
        const response = await BlockAPI.postBlock({
          content: content,
          hidden: false,
          username,
        });
        dispatch(insertBlock({ index: index + 1, block: response.data.data }));
      },
    },
    {
      title: "Toggle Visibility",
      icon: hidden ? <VisibilityOffRoundedIcon /> : <VisibilityRoundedIcon />,
      onClick: () => {
        dispatch(toggleBlockVisibility({ id }));
      },
    },
  ];

  const MENU_OPTIONS = [
    {
      title: "Translate Block",
      onClick: () => {
        setIsTranslateOpened(true);
        handleMenuClose();
      },
    },
    {
      title: "Generate Abstract",
      onClick: () => {
        handleGenerateAbstract();
        handleMenuClose();
      },
    },
    {
      title: "Recording Conversion",
      onClick: () => {
        handleMenuClose();
        handleOpenRecord();
      },
    },
  ];

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
    setIsMenuOpened(!isMenuOpened);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setIsMenuOpened(false);
  };

  const handleBlockUpdate = (content) => {
    dispatch(updateBlockContent({ id, content }));
  };

  const handleTranslateConfirm = async () => {
    setLoading(true);
    const text = content.replace(/<[^>]+>/g, "");
    TranslationAPI.postTranslation(text, language).then((response) => {
      BlockAPI.postBlock({
        content: response.data.data,
        hidden: false,
        username,
      }).then((response) => {
        if (response.status === 200) {
          dispatch(
            insertBlock({ index: index + 1, block: response.data.data })
          );
          setAlert({
            open: true,
            severity: "success",
            msg: "Block translated successfully",
          });
        } else {
          setAlert({
            open: true,
            severity: "error",
            msg: "Block translation failed",
          });
        }
        handleTranslateClose();
      });
    });
  };

  const handleTranslateClose = () => {
    setIsTranslateOpened(false);
    setLanguage("en");
    setLoading(false);
  };

  const handleLanguageChange = (event) => {
    setLanguage(event.target.value);
  };

  const handleGenerateAbstract = () => {
    const text = content.replace(/<[^>]+>/g, "");
    SummaryAPI.postSummary(text).then((response) => {
      BlockAPI.postBlock({
        content: response.data.data,
        hidden: false,
        username,
      }).then((response) => {
        if (response.status === 200) {
          dispatch(
            insertBlock({ index: index + 1, block: response.data.data })
          );
          setAlert({
            open: true,
            severity: "success",
            msg: "Abstract generated successfully",
          });
        } else {
          setAlert({
            open: true,
            severity: "error",
            msg: "Abstract generation failed",
          });
        }
      });
    });
  };

  const handleOpenRecord = () => {
    setIsRecordOpened(true);
  };

  return (
    <>
      <Paper sx={{ width: "100%" }} variant="outlined">
        <CardContent>
          <RichEditor content={content} onUpdate={handleBlockUpdate} />
          <Box
            sx={{
              display: "flex",
              justifyContent: "flex-end",
            }}
          >
            <ButtonGroup variant="text">
              {BLOCK_TOOLBAR.map((button) => (
                <Tooltip key={button.title} title={button.title}>
                  <IconButton id={button.title} onClick={button.onClick}>
                    {button.icon}
                  </IconButton>
                </Tooltip>
              ))}
              <Tooltip title="More">
                <IconButton
                  id="menu-button"
                  onClick={handleMenuClick}
                  aria-controls={isMenuOpened ? "menu" : undefined}
                  aria-haspopup="true"
                  aria-expanded={isMenuOpened ? "true" : undefined}
                >
                  <MoreVertRoundedIcon />
                </IconButton>
              </Tooltip>
            </ButtonGroup>
          </Box>
        </CardContent>
        <Menu
          id="menu"
          anchorEl={anchorEl}
          open={isMenuOpened}
          onClose={handleMenuClose}
          MenuListProps={{
            "aria-labelledby": "menu-button",
          }}
        >
          {MENU_OPTIONS.map((option) => (
            <MenuItem
              key={option.title}
              onClick={option.onClick}
              sx={{ backgroundColor: "rgba(255, 255, 255, 1)" }}
            >
              {option.title}
            </MenuItem>
          ))}
        </Menu>
      </Paper>
      <Dialog open={isTranslateOpened} onClose={handleTranslateClose}>
        <DialogTitle>Translate Block</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please select the language you want to translate to:
          </DialogContentText>
          <br />
          <FormControl fullWidth>
            <InputLabel id="language-select-label">Language</InputLabel>
            <Select
              labelId="language-select-label"
              id="language-select"
              value={language}
              label="Language"
              onChange={handleLanguageChange}
            >
              <MenuItem value={"zh-cn"}>Chinese (Simplified)</MenuItem>
              <MenuItem value={"zh-tw"}>Chinese (Traditional)</MenuItem>
              <MenuItem value={"en"}>English</MenuItem>
              <MenuItem value={"fr"}>French</MenuItem>
              <MenuItem value={"de"}>German</MenuItem>
              <MenuItem value={"it"}>Italian</MenuItem>
              <MenuItem value={"ja"}>Japanese</MenuItem>
              <MenuItem value={"ko"}>Korean</MenuItem>
              <MenuItem value={"pt"}>Portuguese</MenuItem>
              <MenuItem value={"ru"}>Russian</MenuItem>
              <MenuItem value={"es"}>Spanish</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions sx={{ pr: 3, pb: 3 }}>
          {loading ? (
            <CircularProgress size={36} />
          ) : (
            <>
              <Button onClick={handleTranslateClose}>Cancel</Button>
              <Button onClick={handleTranslateConfirm} variant="contained">
                Confirm
              </Button>
            </>
          )}
        </DialogActions>
      </Dialog>
      <RecordingOperator
        index={index}
        open={isRecordOpened}
        setOpen={setIsRecordOpened}
      />
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
