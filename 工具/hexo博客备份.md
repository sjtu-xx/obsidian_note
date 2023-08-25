```yaml

title: hexo博客备份
toc: true
date: 2019-12-09 15:19:03
tags: [hexo]
categories: 
- 其他工具

```

# 备份步骤

<!--more-->

1. 在博客目录下，创建git仓库
`git init`
2. 创建`.gitignore`文件
文件内容：

```
.DS_Store
Thumbs.db
db.json
*.log
node_modules/
public/
.deploy*/

```

1. 将本地与远程仓库关联
`git remote add origin git@github.com:sjtu-xx/sjtu-xx.github.io.git`
2. 设置hexo为默认分支
3. 本地新建hexo分支
`git checkout -b hexo` 创建新分支
4. 上传至github

```bash
git add .
git commit -m "blabla"
git push origin hexo

```

1. hexo g -d 发布博客

## 恢复步骤

1. `git clone https://github.com/sjtu-xx/sjtu-xx.github.io.gito`
2. 在克隆的文件夹下输入`npm install hexo-cli`,`npm install`,`npm install hexo-deployer-git`