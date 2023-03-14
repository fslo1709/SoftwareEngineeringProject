import React, { useState } from "react";
import { useSelector } from "react-redux";
import { selectGlobal } from "../../../slices/globalSlice";

import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  CardContent,
  CircularProgress,
  Grid,
  IconButton,
  Link,
  ListSubheader,
  Paper,
  Tooltip,
  Typography,
} from "@mui/material";
import ExpandMoreRoundedIcon from "@mui/icons-material/ExpandMoreRounded";
import RefreshRoundedIcon from "@mui/icons-material/RefreshRounded";
import { TermAPI } from "../../../api";

export default function Term() {
  const { blocks } = useSelector(selectGlobal);
  const [isLoading, setIsLoading] = useState(false);
  const [terms, setTerms] = useState([]);

  const fetchTerms = async () => {
    setIsLoading(true);
    const stringContent = blocks.map((block) => block.content).join("");
    const text = stringContent.replace(/<[^>]+>/g, "");
    const response = await TermAPI.postTerms(text);
    setTerms(response.data.data);
    setIsLoading(false);
  };

  const handleRefresh = () => {
    fetchTerms();
  };

  return (
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
              variant="h5"
              sx={{
                fontFamily: "Playfair Display",
                fontSize: 20,
                color: "rgba(34, 34, 34, 1)",
                fontWeight: 700,
                ml: 1,
                mt: 1,
              }}
            >
              Terms
            </Typography>
          </Grid>
          <Grid item>
            <Tooltip title="Refresh">
              <IconButton
                size="small"
                onClick={handleRefresh}
                disabled={isLoading}
              >
                <RefreshRoundedIcon />
              </IconButton>
            </Tooltip>
          </Grid>
        </Grid>
      </ListSubheader>
      <CardContent sx={{ pt: 1 }}>
        <div>
          {isLoading ? (
            <Grid
              container
              direction="column"
              justifyContent="center"
              alignItems="center"
              spacing={1}
              sx={{ mt: 2 }}
            >
              <Grid item>
                <CircularProgress />
              </Grid>
              <Grid item>
                <Typography variant="body2">Loading...</Typography>
              </Grid>
            </Grid>
          ) : (
            terms.map((term, index) => (
              <Accordion
                key={"term-" + index}
                elevation={0}
                sx={{ border: "1px solid rgba(0, 0, 0, 0.12)" }}
              >
                <AccordionSummary
                  expandIcon={<ExpandMoreRoundedIcon />}
                  sx={{ margin: 0 }}
                >
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {term.name}
                  </Typography>
                </AccordionSummary>
                <AccordionDetails>
                  <Typography variant="body2">{term.description}</Typography>
                  {term.links.map((link, index) => (
                    <Link key={"link-" + index} href={link} target="_blank">
                      More
                    </Link>
                  ))}
                </AccordionDetails>
              </Accordion>
            ))
          )}
        </div>
      </CardContent>
    </Paper>
  );
}
