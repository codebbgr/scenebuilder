# Release Notes for CbbSceneBuilder 15.0.1

## Introduction

This project is a fork from latest Gluon's Scenebuilder release . In this release we have compiled the source with latest java jdk + latest javafx libaries, adding
the unreleased fixes since latest Gluon's Scenebuilder latest release and provided some more fixes as well.

## List of New features
Issue key|Summary
---------|-------
| [#171](https://github.com/gluonhq/scenebuilder/issues/171)| Disable accordion menu animation 
| |Scan test resources for controller class
| [#124](https://github.com/gluonhq/scenebuilder/issues/124)| Wildcard import enable/disable option in preferences
| [#214](https://github.com/gluonhq/scenebuilder/pull/214)| Move SelectionBar css file to kit
| | Move InfoPanelController to kit module
| | UserLibrary: feature for adding folders to library
| | Added logging when exploring libraries (jars or folders), including refactoring of JarExplorer and FolderExplorer
| | Create components module
| | Added ExternalSectionProvider and remove Gluon built-in controls
| | Add GluonSectionProvider and move built-in controls to Components
| | Add ExternalMetadataProvider
| | Removed deprecated XMLReader with SAXParserFactory 
|[#240](https://github.com/gluonhq/scenebuilder/issues/240)| JPMS (modules) support
| | Arrays.asList , List.toArray replaced with java8 streams 






## List of Fixed Bugs

Issue key|Summary
---------|-------
| [#84](https://github.com/gluonhq/scenebuilder/issues/84)| Store Color as a tag instead of String 
| [#207](https://github.com/gluonhq/scenebuilder/issues/207)| Refresh the scene graph every time fxomDocument did change
