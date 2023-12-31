---
title: Focal loss原理与实现
toc: true
date: 2020-02-22 14:29:39
tags: 
categories:
- 深度学习
- 基础理论
---

# 什么是Focal loss
<!--more-->
Focal loss是何恺明大神提出的一种新的loss计算方案。
其具有两个重要的特点。

1、控制正负样本的权重
2、控制容易分类和难分类样本的权重

正负样本的概念如下：
一张图像可能生成成千上万的候选框，但是其中只有很少一部分是包含目标的的，有目标的就是正样本，没有目标的就是负样本。

容易分类和难分类样本的概念如下：
假设存在一个二分类，样本1属于类别1的pt=0.9，样本2属于类别1的pt=0.6，显然前者更可能是类别1，其就是容易分类的样本；后者有可能是类别1，所以其为难分类样本。

如何实现权重控制呢，请往下看：
# 控制正负样本的权重
如下是常用的交叉熵loss，以二分类为例：
![](_attachments/d5d49a3bde157c852167d4ec0d6118b2.png)
我们可以利用如下Pt简化交叉熵loss。
![](_attachments/2620c6cb7f02e188d68ff4dc8ce1c601.png)
此时：
![](_attachments/6844345dc3f10ab7dcb45e1ece469804.png)
想要降低负样本的影响，可以在常规的损失函数前增加一个系数αt。与Pt类似，当label=1的时候，αt=α；当label=otherwise的时候，αt=1 - α，a的范围也是0到1。此时我们便可以通过设置α实现控制正负样本对loss的贡献。
![](_attachments/94ee9b27c2e8e8ba12c2153e2cf172d5.png)
其中：
![](_attachments/0d63196d158ca181fa19bc70c50d185a.png)
分解开就是：
![](_attachments/c814d84aee65291e2669b0ec557acaf3.jpg)

# 控制容易分类和难分类样本的权重
按照刚才的思路，一个二分类，样本1属于类别1的pt=0.9，样本2属于类别1的pt=0.6，也就是 是某个类的概率越大，其越容易分类 所以利用1-Pt就可以计算出其属于容易分类或者难分类。
具体实现方式如下。
![](_attachments/431abf54c59eedb7b1ead2b67164b038.png)
其中$$(1-p_t)^{\gamma}$$称为调制系数
1、当pt趋于0的时候，调制系数趋于1，对于总的loss的贡献很大。当pt趋于1的时候，调制系数趋于0，也就是对于总的loss的贡献很小。
2、当γ=0的时候，focal loss就是传统的交叉熵损失，可以通过调整γ实现调制系数的改变。

# 两种权重控制方法合并
通过如下公式就可以实现控制正负样本的权重和控制容易分类和难分类样本的权重。
![](_attachments/36d6140907b2871bcbe58e766e99c304.png)

