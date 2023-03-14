import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  // audio operation
  audioFiles: [],

  // block operation
  blocks: [],
};

export const globalSlice = createSlice({
  name: "global",
  initialState,
  reducers: {
    // audio operation
    setAudioFiles: (state, action) => {
      state.audioFiles = action.payload;
    },
    setBlocks: (state, action) => {
      state.blocks = action.payload;
    },
    addBlock: (state, action) => {
      state.blocks = [...state.blocks, action.payload];
    },
    moveBlockUp: (state, action) => {
      const { id } = action.payload;
      const index = state.blocks.findIndex((block) => block.id === id);
      if (index > 0) {
        // immutable swap
        let blocksCopy = [...state.blocks];
        const temp = state.blocks[index - 1];
        blocksCopy[index - 1] = state.blocks[index];
        blocksCopy[index] = temp;
        state.blocks = blocksCopy;
      }
    },
    moveBlockDown: (state, action) => {
      const { id } = action.payload;
      const index = state.blocks.findIndex((block) => block.id === id);
      if (index < state.blocks.length - 1) {
        const temp = state.blocks[index + 1];
        state.blocks[index + 1] = state.blocks[index];
        state.blocks[index] = temp;
      }
    },
    insertBlock: (state, action) => {
      const { index, block } = action.payload;
      state.blocks = [
        ...state.blocks.slice(0, index),
        block,
        ...state.blocks.slice(index),
      ];
    },
    updateBlockContent: (state, action) => {
      const { id, content } = action.payload;
      const index = state.blocks.findIndex((block) => block.id === id);
      state.blocks[index].content = content;
    },
    deleteBlock: (state, action) => {
      const { id } = action.payload;
      const index = state.blocks.findIndex((block) => block.id === id);
      state.blocks = [
        ...state.blocks.slice(0, index),
        ...state.blocks.slice(index + 1),
      ];
    },
    toggleBlockVisibility: (state, action) => {
      const { id } = action.payload;
      const index = state.blocks.findIndex((block) => block.id === id);
      state.blocks[index].hidden = !state.blocks[index].hidden;
    },
    mergeBlocks: (state, action) => {
      const { ids, newBlock } = action.payload;
      const index = state.blocks.findIndex((block) => block.id === ids[0]);
      // remove blocks in ids
      state.blocks = state.blocks.filter((block) => !ids.includes(block.id));
      // insert new block
      state.blocks = [
        ...state.blocks.slice(0, index),
        newBlock,
        ...state.blocks.slice(index),
      ];
    },
    hideBlocks: (state, action) => {
      const { ids } = action.payload;
      state.blocks = state.blocks.map((block) => {
        if (ids.includes(block.id)) {
          return { ...block, hidden: true };
        } else {
          return block;
        }
      });
    },
    showBlocks: (state, action) => {
      const { ids } = action.payload;
      state.blocks = state.blocks.map((block) => {
        if (ids.includes(block.id)) {
          return { ...block, hidden: false };
        } else {
          return block;
        }
      });
    },
    deleteBlocks: (state, action) => {
      const { ids } = action.payload;
      state.blocks = state.blocks.filter((block) => !ids.includes(block.id));
    },
    resetState: () => {
      return initialState;
    },
  },
});

export const {
  setAudioFiles,
  setBlocks,
  moveBlockUp,
  moveBlockDown,
  insertBlock,
  updateBlockContent,
  deleteBlock,
  toggleBlockVisibility,
  mergeBlocks,
  hideBlocks,
  showBlocks,
  deleteBlocks,
  addBlock,
  resetState,
} = globalSlice.actions;

export const selectGlobal = (state) => state.global;

export default globalSlice.reducer;
