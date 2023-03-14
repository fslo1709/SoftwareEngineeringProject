import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import {
  Button,
  ButtonGroup,
  Checkbox,
  List,
  ListItem,
  ListItemIcon,
  ListSubheader,
  Paper,
} from "@mui/material";
import MergeRoundedIcon from "@mui/icons-material/MergeRounded";
import VisibilityOffRoundedIcon from "@mui/icons-material/VisibilityOffRounded";
import VisibilityRoundedIcon from "@mui/icons-material/VisibilityRounded";
import DeleteRoundedIcon from "@mui/icons-material/DeleteRounded";
import AddRoundedIcon from "@mui/icons-material/AddRounded";
import Block from "./block";
import {
  addBlock,
  deleteBlocks,
  hideBlocks,
  mergeBlocks,
  selectGlobal,
  showBlocks,
} from "../../../slices/globalSlice";
import { BlockAPI } from "../../../api";
import { selectSession } from "../../../slices/sessionSlice";

export default function Board() {
  const dispatch = useDispatch();
  const { username } = useSelector(selectSession);
  const { blocks } = useSelector(selectGlobal);
  const [selected, setSelected] = useState([]);

  const handleSelectAll = () => {
    setSelected((prevSelected) => {
      if (prevSelected.length === blocks.length) {
        return [];
      }
      return blocks.map((block) => block.id);
    });
  };

  const handleSelect = (id) => {
    if (selected.includes(id)) {
      setSelected(selected.filter((item) => item !== id));
    } else {
      setSelected([...selected, id]);
    }
  };

  const handleMergeSelected = async () => {
    const newBlock = {
      content: selected
        ?.map((id) => blocks.find((block) => block.id === id).content)
        .join(""),
      hidden: false,
      username,
    };
    const response = await BlockAPI.postBlock(newBlock);
    await BlockAPI.deleteBlocks(selected);
    dispatch(mergeBlocks({ ids: selected, newBlock: response.data.data }));
    setSelected([]);
  };

  const handleHideSelected = () => {
    dispatch(hideBlocks({ ids: selected }));
    setSelected([]);
  };

  const handleShowSelected = () => {
    dispatch(showBlocks({ ids: selected }));
    setSelected([]);
  };

  const handleDeleteSelected = async () => {
    await BlockAPI.deleteBlocks(selected);
    dispatch(deleteBlocks({ ids: selected }));
    setSelected([]);
  };

  const handleAddBlock = async () => {
    const response = await BlockAPI.postBlock({
      content: "",
      hidden: false,
      username,
    });
    dispatch(addBlock(response.data.data));
  };

  useEffect(() => {
    async function saveBlocks() {
      await BlockAPI.putBlocks(blocks);
    }
    saveBlocks();
  }, [blocks]);

  return (
    <div>
      <Paper sx={{ height: "85vh", overflow: "scroll" }} variant="outlined">
        <List sx={{ pt: 0 }}>
          <ListSubheader
            key="list-menu"
            sx={{
              pt: 2,
              pb: 1,
              mb: 1,
              zIndex: 950,
              backgroundColor: "rgba(255, 255, 255, 1)",
              borderBottom: "1px solid rgba(0, 0, 0, 0.12)",
            }}
          >
            <ListItemIcon>
              <Checkbox
                checked={
                  selected.length !== 0 && selected.length === blocks.length
                }
                onChange={handleSelectAll}
              />
            </ListItemIcon>
            <ButtonGroup>
              <Button
                startIcon={<MergeRoundedIcon />}
                onClick={handleMergeSelected}
                variant="outlined"
              >
                Merge
              </Button>
              <Button
                startIcon={<VisibilityRoundedIcon />}
                onClick={handleShowSelected}
                variant="outlined"
              >
                Show
              </Button>
              <Button
                startIcon={<VisibilityOffRoundedIcon />}
                onClick={handleHideSelected}
                variant="outlined"
              >
                Hide
              </Button>

              <Button
                startIcon={<DeleteRoundedIcon />}
                onClick={handleDeleteSelected}
                color="secondary"
                variant="outlined"
              >
                Delete
              </Button>
            </ButtonGroup>
          </ListSubheader>
          {blocks?.map((block, index) => (
            <ListItem key={"list-item-" + block.id}>
              <ListItemIcon>
                <Checkbox
                  checked={selected.includes(block.id)}
                  onChange={() => handleSelect(block.id)}
                />
              </ListItemIcon>
              <Block
                key={block.id}
                index={index}
                id={block.id}
                content={block.content}
                hidden={block.hidden}
              />
            </ListItem>
          ))}
          <ListItem key="list-add" sx={{ justifyContent: "center" }}>
            <Button
              variant="contained"
              startIcon={<AddRoundedIcon />}
              onClick={handleAddBlock}
              fullWidth
            >
              Add Block
            </Button>
          </ListItem>
        </List>
      </Paper>
    </div>
  );
}
