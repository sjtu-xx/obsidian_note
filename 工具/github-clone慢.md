```yaml

title: github_clone慢
toc: true
mathjax: true
date: 2021-02-04 00:13:03
tags:
categories:
- CODE工具

```

最近在使用github时，发现从服务器clone到本地非常慢，查找了很多解决方案，最终决定使用配置git代理的方式来进行实现。

<!--more-->

git clone慢：
一种简单的处理办法：
只需要将 www.github.com/后面为代码库 改为

www.github.com.cnpmjs.org/后面为代码库地址 就可以实现一键式加速。

代理：

```
git config --global --unset http.proxy
git config --global --unset https.proxy

git config --global http.https://github.com.proxy https://127.0.0.1:10809
git config --global https.https://github.com.proxy https://127.0.0.1:10809

git config --global http.https://github.com.proxy socks5://127.0.0.1:10808
git config --global https.https://github.com.proxy socks5://127.0.0.1:10808

```