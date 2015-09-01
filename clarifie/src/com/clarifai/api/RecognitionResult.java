package com.clarifai.api;

import static com.clarifai.api.ClarifaiRequester.GSON;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Recognition results for a single image. See https://developer.clarifai.com for complete
 * documentation on the Clarifai image recognition API.
 */
public class RecognitionResult {
  static class Deserializer implements JsonDeserializer<RecognitionResult> {
    public RecognitionResult deserialize(JsonElement json, Type type,
        JsonDeserializationContext context) throws JsonParseException {
      return new RecognitionResult(GSON.fromJson(json, MediaResultMessage.class));
    }
  }

  static class MediaResultMessage {
    private String statusCode;
    private String statusMsg;
    private String docid;
    private ResultMessage result;
  }

  static class ResultMessage {
    private TagMessage tag;
    private double[] embed;
  }

  static class TagMessage {
    private String[] classes;
    private double[] probs;
  }

  /** Indicates the status of the request. */
  public static enum StatusCode {
    /** The recognition operation completed successfully. */
    OK,
    /** There was a problem with the input provided by the caller. */
    CLIENT_ERROR,
    /** There was an error on the server processing the request.. */
    SERVER_ERROR,
  }

  private final StatusCode statusCode;
  private final String statusMessage;
  private final String docId;
  private List<Tag> tags;
  private double[] embedding;

  private RecognitionResult(MediaResultMessage message) {
    if ("OK".equals(message.statusCode)) {
      statusCode = StatusCode.OK;
    } else if ("CLIENT_ERROR".equals(message.statusCode)) {
      statusCode = StatusCode.CLIENT_ERROR;
    } else {  // Treat unknown status code as server error.
      statusCode = StatusCode.SERVER_ERROR;
    }
    statusMessage = message.statusMsg;
    docId = message.docid;
    if (message.result != null) {
      embedding = message.result.embed;
      if (message.result.tag != null) {
        TagMessage tag = message.result.tag;
        if (tag.classes != null && tag.probs != null) {
          int count = Math.min(tag.classes.length, tag.probs.length);
          tags = new ArrayList<Tag>(count);
          for (int i = 0; i < count; i++) {
            tags.add(new Tag(tag.classes[i], tag.probs[i]));
          }
        }
      }
    }
  }

  /** Returns the status of the request. */
  public StatusCode getStatusCode() {
    return statusCode;
  }

  /** Returns additional information about the status of the request. */
  public String getStatusMessage() {
    return statusMessage;
  }

  /** Returns a unique and stable identifier for the content. */
  public String getDocId() {
    return docId;
  }

  /** Returns a list of {@link Tag}s or null if tags were not requested or the request failed. */
  public List<Tag> getTags() {
    return tags;
  }

  /** Returns embedding vector or null if embeddings were not requested or the request failed. */
  public double[] getEmbedding() {
    return embedding;
  }
}
