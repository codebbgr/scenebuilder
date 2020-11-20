/*
 * Copyright (c) 2020, codebb.gr and/or its affiliates.
 * Copyright (c) 2015, 2016, 2017 Gluon and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates
 * Check license.txt for license
 */
package com.oracle.javafx.scenebuilder.app.about;

import com.oracle.javafx.scenebuilder.app.SceneBuilderApp;
import com.oracle.javafx.scenebuilder.app.i18n.I18N;
import com.oracle.javafx.scenebuilder.app.util.AppSettings;
import com.oracle.javafx.scenebuilder.kit.editor.panel.util.AbstractFxmlWindowController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

/** */
public final class AboutWindowController extends AbstractFxmlWindowController {

  @FXML private GridPane vbox;
  @FXML private TextArea textArea;

  private String sbBuildInfo;
  private String sbBuildVersion;
  private String sbBuildDate;
  private String sbBuildJavaVersion;
  // The resource bundle contains two keys: about.copyright and about.copyright.open
  private String sbAboutCopyrightKeyName;
  // File name must be in sync with what we use in logging.properties (Don't understand this
  // comment, haven't found any logging.properties file
  private final String LOG_FILE_NAME =
      "scenebuilder-" + AppSettings.getSceneBuilderVersion() + ".log"; // NOI18N

  public AboutWindowController() {
    super(
        AboutWindowController.class.getResource("About.fxml"), // NOI18N
        I18N.getBundle());
    try (InputStream in = getClass().getResourceAsStream("about.properties")) { // NOI18N

      if (in != null) {
        Properties sbProps = new Properties();
        sbProps.load(in);
        sbBuildInfo = sbProps.getProperty("build.info", "UNSET"); // NOI18N
        sbBuildVersion = sbProps.getProperty("build.version", "UNSET"); // NOI18N
        sbBuildDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        sbBuildJavaVersion =
            System.getProperty("java.runtime.version") + ", " + System.getProperty("java.vendor");
        sbAboutCopyrightKeyName = sbProps.getProperty("copyright.key.name", "UNSET"); // NOI18N
      }
    } catch (IOException ex) {
      // We go with default values
    }
  }

  @FXML
  public void onMousePressed(MouseEvent event) {
    if ((event.getClickCount() == 2) && event.isAltDown()) {
      SceneBuilderApp.getSingleton().toggleDebugMenu();
    }
  }

  @Override
  public void onCloseRequest(WindowEvent event) {
    closeWindow();
  }

  /*
   * AbstractWindowController
   */
  @Override
  protected void controllerDidCreateStage() {
    assert getRoot() != null;
    assert getRoot().getScene() != null;
    assert getRoot().getScene().getWindow() != null;

    getStage().setTitle(I18N.getString("about.title"));
    getStage().initModality(Modality.APPLICATION_MODAL);
  }

  @Override
  protected void controllerDidLoadFxml() {
    super.controllerDidLoadFxml();
    assert vbox != null;
    assert textArea != null;
    textArea.setText(getAboutText());
  }

  private String getAboutText() {

    StringBuilder text =
        getVersionParagraph()
            .append(getBuildInfoParagraph())
            .append(getLoggingParagraph())
            .append(getJavaParagraph())
            .append(getOsParagraph())
            .append(I18N.getString(sbAboutCopyrightKeyName));

    return text.toString();
  }

  /** @treatAsPrivate */
  public String getBuildJavaVersion() {
    return sbBuildJavaVersion;
  }

  /** @treatAsPrivate */
  public String getBuildInfo() {
    return sbBuildInfo;
  }

  private StringBuilder getVersionParagraph() {
    StringBuilder sb = new StringBuilder(I18N.getString("about.product.version"));
    sb.append("\nJavaFX Scene Builder ")
        .append(sbBuildVersion) // NOI18N
        .append("\n\n"); // NOI18N
    return sb;
  }

  private String getLogFilePath() {
    StringBuilder sb = new StringBuilder(System.getProperty("java.io.tmpdir")); // NOI18N
    if (sb.charAt(sb.length() - 1) != File.separatorChar) {
      sb.append(File.separatorChar);
    }
    sb.append(LOG_FILE_NAME);
    return sb.toString();
  }

  private StringBuilder getBuildInfoParagraph() {
    StringBuilder sb = new StringBuilder(I18N.getString("about.build.information"));
    sb.append("\n")
        .append(sbBuildInfo)
        .append("\n") // NOI18N
        .append(I18N.getString("about.build.date", sbBuildDate))
        .append("\n")
        .append(I18N.getString("about.build.java.version", sbBuildJavaVersion))
        .append("\n\n"); // NOI18N
    return sb;
  }

  private StringBuilder getLoggingParagraph() {
    StringBuilder sb = new StringBuilder(I18N.getString("about.logging.title"));
    sb.append("\n") // NOI18N
        .append(I18N.getString("about.logging.body.first", LOG_FILE_NAME))
        .append("\n") // NOI18N
        .append(I18N.getString("about.logging.body.second", getLogFilePath()))
        .append("\n\n"); // NOI18N
    return sb;
  }

  private StringBuilder getJavaParagraph() {
    StringBuilder sb = new StringBuilder("Java\n"); // NOI18N
    sb.append(System.getProperty("java.runtime.version"))
        .append(", ") // NOI18N
        .append(System.getProperty("java.vendor"))
        .append("\n\n"); // NOI18N
    return sb;
  }

  private StringBuilder getOsParagraph() {
    StringBuilder sb = new StringBuilder(I18N.getString("about.operating.system"));
    sb.append("\n")
        .append(System.getProperty("os.name"))
        .append(", ") // NOI18N
        .append(System.getProperty("os.arch"))
        .append(", ") // NOI18N
        .append(System.getProperty("os.version"))
        .append("\n\n"); // NOI18N
    return sb;
  }
}
