```yaml

title: git不再使用密码进行认证
toc: true
mathjax: true
date: 2021-06-06 18:31:56
tags:
categories:
- CODE工具

```

最近在部署博客的过程中，收到了不在提供密码认证的邮件，内容如下，需要解决。

<!--more-->

```
Hi @sjtu-xx,

You recently used a password to access the repository at sjtu-xx/sjtu-xx.github.io with git using git/2.31.1.

Basic authentication using a password to Git is deprecated and will soon no longer work. Visit https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/ for more information around suggested workarounds and removal dates.

Thanks,
The GitHub Team

```

根据邮件中的链接说明，需要使用令牌进行认证。

1. 令牌创建：
https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token#using-a-token-on-the-command-line

使用Mac搜索“keychain”并打开钥匙串,
搜索GitHub。您可能会看到两个选项：应用程序密码和互联网密码
打开应用程序密码。您可以选中“显示密码”框，并可能看到这仍然使用您的密码，而不是令牌。

1. 清除之前的登陆信息
- 取消设置登录凭据。将此复制到您的终端，然后点击两次回车

```
git config --global --unset user.name
git config --global --unset user.email
git config --global --unset credential.helper
git credential-osxkeychain erase
host=github.com
protocol=https
# Hit return/enter twice

```

- 步骤2

打开一个新的终端窗口，并设置您的姓名和电子邮件：

```
git config --global user.name "Your Name"
git config --global user.email you@example.com

```

- 步骤3

```
Make a small local change to one of your repositories and git add ., then git commit -m "Your small change", then finally git push and you'll be asked for your GitHub username, in that field enter the email address for your GitHub account.

```

在下一行中，系统会要求您提供密码，请在此处粘贴令牌（即从您创建令牌的GitHub复制并粘贴到其中），就像这样：

```
git push
Username for 'https://github.com': you@example.com
Password for 'https://you@example.com@github.com': 

```

您的提交将推送，您现在使用的是令牌身份验证，而不是密码身份验证。仅此而已。