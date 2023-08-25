## 在线安装
1、

## 离线安装
1、离线安装
[Offline install of oh-my-zsh on Ubuntu · GitHub](https://gist.github.com/hewerthomn/65bb351bf950470f6c9e6aba8c0c04f1#file-install-oh-my-zsh-sh)
2、安装插件
[GitHub - zsh-users/zsh-syntax-highlighting: Fish shell like syntax highlighting for Zsh.](https://github.com/zsh-users/zsh-syntax-highlighting)
[GitHub - zsh-users/zsh-autosuggestions: Fish-like autosuggestions for zsh](https://github.com/zsh-users/zsh-autosuggestions)

下载完成后将插件移动到安装目录
```zsh
cp -r zsh-autosuggestions-master .oh-my-zsh/custom/plugins/zsh-autosuggestions
cp -r zsh-syntax-highlighting-master .oh-my-zsh/custom/plugins/zsh-syntax-highlighting
```

在`.zshrc`配置中增加插件
```
plugins=( 
    # other plugins...
    zsh-autosuggestions
    zsh-syntax-highlighting
)
```
