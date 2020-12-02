/*
 * Copyright (c) 2020, codebb.gr and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates
 * Check license.txt for license
 */
package com.oracle.javafx.scenebuilder.app;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;

/** */
public class SplitController {

  public enum Target {
    FIRST,
    LAST
  };

  private final SplitPane splitPane;
  private final Target target;
  private final Node targetNode;
  private double dividerPosition = -1.0;

  public SplitController(SplitPane splitPane, Target target) {
    assert splitPane != null;
    assert splitPane.getItems().size() >= 1;

    this.splitPane = splitPane;
    this.target = target;

    final List<Node> children = splitPane.getItems();
    final int targetIndex = (target == Target.FIRST) ? 0 : children.size() - 1;
    this.targetNode = children.get(targetIndex);
  }

  public DoubleProperty position() {
    final Divider divider = getTargetDivider();
    return divider == null ? null : divider.positionProperty();
  }

  public double getPosition() {
    final Divider divider = getTargetDivider();
    return divider == null ? -1.0 : divider.getPosition();
  }

  public void setPosition(double value) {
    final Divider divider = getTargetDivider();
    if (divider != null) {
      divider.setPosition(value);
    }
    dividerPosition = value;
  }

  public void showTarget() {
    if (isTargetVisible() == false) {
      // Put the target node back in the split pane items
      if (target == Target.FIRST) {
        splitPane.getItems().add(0, targetNode);
      } else {
        splitPane.getItems().add(targetNode);
      }

      // Restore the target divider position (if any)
      final List<Divider> dividers = splitPane.getDividers();
      if ((dividers.isEmpty() == false) && (dividerPosition != -1)) { // (1)
        final Divider divider = getTargetDivider();
        assert divider != null; // Because of (1)
        divider.setPosition(dividerPosition);
      }
    }
  }

  public void hideTarget() {

    if (isTargetVisible()) {

      final List<Divider> dividers = splitPane.getDividers();
      final List<Double> positionsList =
          DoubleStream.of(splitPane.getDividerPositions()).boxed().collect(Collectors.toList());
      // Backup the target divider positions (if any)
      // so we can restore it on showing
      final Divider targetDivider = getTargetDivider();
      if (targetDivider != null) {
        dividerPosition = targetDivider.getPosition();
        int targetDividerIndex = target == Target.FIRST ? 0 : dividers.size() - 1;
        positionsList.remove(targetDividerIndex);
      }

      // Removes the target node from the split pane items
      splitPane.getItems().remove(targetNode);

      // Set back remaining dividers positions if any
      if (positionsList.isEmpty() == false) {
        double[] positionsArray = positionsList.stream().mapToDouble(Double::doubleValue).toArray();
        splitPane.setDividerPositions(positionsArray);
      }
    }
  }

  public void toggleTarget() {
    if (isTargetVisible()) {
      hideTarget();
    } else {
      showTarget();
    }
  }

  public void setTargetVisible(boolean visible) {
    if (visible) {
      showTarget();
    } else {
      hideTarget();
    }
  }

  public boolean isTargetVisible() {
    return splitPane.getItems().contains(targetNode);
  }

  private Divider getTargetDivider() {
    final Divider divider;
    final List<Divider> dividers = splitPane.getDividers();
    if (dividers.isEmpty() == false) {
      if (target == Target.FIRST) {
        divider = dividers.get(0);
      } else {
        divider = dividers.get(dividers.size() - 1);
      }
    } else {
      divider = null;
    }
    return divider;
  }
}
