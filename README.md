# clean-code-sweep

![Build](https://github.com/kuweiguge/clean-code-sweep/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/21947-clean-code-sweep.svg)](https://plugins.jetbrains.com/plugin/21947-clean-code-sweep)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/21947-clean-code-sweep.svg)](https://plugins.jetbrains.com/plugin/21947-clean-code-sweep)
[![Slack](https://img.shields.io/badge/Slack-%23WinterCode-blue?style=flat-square&logo=Slack)](https://wintercodehq.slack.com)
<!-- Plugin description -->
## Introduction
A collection of small code tools.<br>
call menu shortcut key：<b>ctrl+alt+c</b><br>
All functions can be used at the current file level, selected file level, and selected folder level. They will take effect on all selected files.<br>
More small functions are under development. Welcome to [Slack](https://wintercodehq.slack.com) or GitHub issue for suggestions.
## ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Add Function: Remove blank lines from the file.
- [x] Add Function: Add @param annotations to the method parameters of the Mapper interface file, automatically extract the method parameter names as the value of the @Param annotation.
- [x] Add Function: Convert single-line comments of class attributes to JavaDoc comments. (Many one-click document generation plug-ins, such as FastRequest, are based on JavaDoc comments, so this function can be used with these plug-ins)
- [x] Add Function: Add annotations to class attributes according to the document comments of the class attributes, such as: generate @ApiModelProperty annotations, @ExcelProperty annotations, @JsonProperty annotations, etc. according to document comments.
- [ ] Add Function: something else...
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "clean-code-sweep"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/kuweiguge/clean-code-sweep/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation