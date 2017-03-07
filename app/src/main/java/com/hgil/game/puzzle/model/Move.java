package com.hgil.game.puzzle.model;

/**
 * Represents a tile move on the puzzle board. The rules 
 * allow to move only the tiles adjacent to a blank tile.
 * Each move swaps the tile with the blank, effectively
 * moving the blank in one of the four directions.
 * These directions are available as constants of this
 * class.
 */
public enum Move
{
 UP(false, -1), LEFT(true, -1), DOWN(false, 1), RIGHT(true, 1);

 public boolean isHorizontal()
 {
  return horizontal;
 }

 public boolean isVertical()
 {
  return !horizontal;
 }

 public int getAmount()
 {
  return amount;
 }

 @Override
 public String toString()
 {
  return name().toLowerCase() + " move";
 }

 private Move(boolean horizontal, int amount)
 {
  this.horizontal = horizontal;
  this.amount = amount;
 }

 private boolean horizontal; 
 private int amount; 
}
