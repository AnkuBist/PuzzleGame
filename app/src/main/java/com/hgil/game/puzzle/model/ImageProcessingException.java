package com.hgil.game.puzzle.model;

/**
 * Indicates a problem loading or processing a puzzle's graphics.
 */
public class ImageProcessingException extends Exception
{
 public ImageProcessingException(String message)
 {
  super(message);
 }

 public ImageProcessingException(String message, Throwable cause)
 {
  super(message, cause);
 }

 private static final long serialVersionUID = 1L;
}
