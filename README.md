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
## Function list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Remove blank lines from the file.
- [x] Add @param annotations to the method parameters of the Mapper interface file, automatically extract the method parameter names as the value of the @Param annotation.
- [x] Convert single-line comments of class attributes to JavaDoc comments. (Many one-click document generation plug-ins, such as FastRequest, are based on JavaDoc comments, so this function can be used with these plug-ins)
- [x] Add annotations to class attributes according to the document comments of the class attributes, such as: generate @ApiModelProperty annotations, @ExcelProperty annotations, @JsonProperty annotations, etc.
- [x] Add annotations to class attributes according to the class attributes name, such as: generate @ApiModelProperty annotations, @ExcelProperty annotations, @JsonProperty annotations, etc.
- [ ] Add Function: something else...

## 介绍
一组小型代码工具。<br>
菜单快捷键：<b>ctrl+alt+c</b><br>
所有功能都可以在当前文件级别、选定的文件级别和选定的文件夹级别使用。它们将**对所有选定的文件生效**。<br>
更多的小功能正在开发中， 欢迎到 [Slack](https://wintercodehq.slack.com) 或者 GitHub issue 提出您的建议。
## 功能列表
- [x] 从 [IntelliJ Platform Plugin Template][template] 创建项目.
- [x] 从文件中删除空行。
- [x] 在Mapper接口文件的方法参数中添加@param注释，自动提取方法参数名称作为@Param注释的值。
- [x] 将类属性的单行注释转换为JavaDoc注释。(许多一键文档生成插件，如FastRequest，都是基于JavaDoc注释的，因此可以与这些插件一起使用此功能)
- [x] 根据类属性的**文档注释**添加注解，例如：生成 @ApiModelProperty 注解、@ExcelProperty 注解、@JsonProperty 注解等，以及自定义注解。
- [x] 根据类**属性名称**添加注释，例如：生成 @ApiModelProperty 注释、@ExcelProperty 注释、@JsonProperty 注释等，以及自定义注解。
- [ ] 更多功能开发中...
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