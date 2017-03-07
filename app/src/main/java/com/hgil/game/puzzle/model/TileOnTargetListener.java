package com.hgil.game.puzzle.model;

/**
 * Allows implementors to receive notifications of
 * tiles changing their {@link Tile#isOnTarget() on target}
 * status.
 */
public interface TileOnTargetListener
{
 /**
  * Receives notifications of
  * tiles changing their {@link Tile#isOnTarget() on target}
  * status.
  * @param tile the tile that changed status
  * @param onTarget the new {@link Tile#isOnTarget() on target status}
  * value
  */
 public void tileOnTargetStateChanged(Tile tile, boolean onTarget);
}
