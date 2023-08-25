```yaml

title: mac创建rar压缩文件
toc: true
mathjax: true
date: 2021-03-03 17:21:40
tags:
categories:
- 其他工具

```

最近在提交论文材料，网站只支持上传rar格式的压缩文件，之前下载的软件均不支持打包为rar格式。经过一番查找，最终决定使用brew安装压缩命令。

<!--more-->
1. 通过homebrew安装rar压缩命令：`brew install --cask rar`(解压为`brew install --cask unrar`)
2. 压缩文件：`rar a all <文件列表>`(将文件列表压缩到`all.rar文件)