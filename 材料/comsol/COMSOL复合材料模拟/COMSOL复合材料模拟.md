---
title: COMSOL复合材料模拟
toc: true
mathjax: true
date: 2020-08-16 21:57:13
tags: [COMSOL, 复合材料]
categories:
- 材料
---

参考链接：https://cn.comsol.com/model/micromechanics-and-stress-analysis-of-a-composite-cylinder-67001
<!--more-->
# COMSOL复合材料模拟

纤维复合材料通常为薄层材料，纤维在层中呈单一取向。

薄层使用两种方法模拟：Layerwise (LW) theory，Equivalent Single Layer (ESL)
theory

## 模型定义

对符合材料的分析内容包含以下几个部分：

- 微机械分析
- 使用LW理论进行应力分析
- 使用ESL理论进行应力分析

### 微机械分析

假设复合材料由碳纤维单向排列在环氧树脂基体中。碳纤维占据了60%的体积。

![](_attachments/734db0c60c9e648d499b9baf0226c69f.jpg)

#### 材料属性

碳纤维T300假设为横观各向同性（正交）；环氧树脂假设为各向同性

|材料性能|值|
|-------|-----|
|{% raw %}$E_1,E_2,E_3$ {% endraw %}|{230,15,15}GPa|
|{% raw %}$G_{12},G_{23},G_{13}$ {% endraw %}|{15,7,15}GPa|
|{% raw %}$\upsilon_{12}.\upsilon_{23},\upsilon_{13}${% endraw %}|{0.2,0.07,0.2}|
|$rho$|1800$kg/m^3$|


|材料性能|值|
|-------|-----|
|E|4GPa|
|$\upsilon$|0.35|
|$\rho$|1100$kg/m^3$|

#### cell周期性
使用cell periodicity节点应用周期性边界条件。

### 基于LW理论的应力分析
#### LW理论
LW理论考虑了表面以及厚度方向的位移自由度。从本构方程角度看，类似于三维固体。适用于厚板。

#### 几何和边界条件
<b>几何</b>

长0.5m,半径0.1m的圆柱

<b>边界条件</b>

- 圆柱体的一端固定
- 另一端有滚柱支撑
- 1kN的力施加在1/4外表面

![](_attachments/b216da75e81fdffe4e5e41d999c0661a.jpg)

<b>堆叠顺序和材料属性</b>

材料由5层1mm厚的层组成。不同层纤维取向不同，0，45，90，-45，0

### ESL理论进行应力分析
ESL理论中考虑了薄层中面的位移和旋转自由度（类似与3D壳单元），因此计算量比LW理论要小得多。适用于薄板、中厚板。



## 建模过程

> 选择周期性边界条件后，在周期性边界的右上角可以自动创建研究和材料。






