## 一.在构建镜像时修改时区

在构建镜像时修改时区就没有必要在容器运行时进行修改了，比较简单。

1.  基于`Debian`的镜像，直接添加环境变量即可：
```ini
ENV TZ=Asia/Shanghai
```

2.  基于`Alpine`的镜像 与 Debian 镜像不同，此类镜像中并没有包含tzdata，所以只设置环境变量并不能达到我们想要的效果，因此需要安装tzdata。

```bash
ENV TZ=Asia/Shanghai
RUN apk update \
    && apk add tzdata \
    && echo "${TZ}" > /etc/timezone \
    && ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \
    && rm /var/cache/apk/*
```

3.  基于`ubuntu`的镜像 与 Debian 镜像不同，此类镜像中并没有包含tzdata，所以只设置环境变量并不能达到我们想要的效果，因此需要安装tzdata。
```bash
ENV TZ=Asia/Shanghai 
RUN echo "${TZ}" > /etc/timezone \ 
&& ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime \ 
&& apt update \ 
&& apt install -y tzdata \ 
&& rm -rf /var/lib/apt/lists/*
```

## 二.在启动容器时修改时区

```ini
docker run -e TZ=Asia/Shanghai .........   
```

## 三.在容器运行时修改时区
```bash
docker exec -it -u root 容器名或id /bin/sh
mkdir -p /usr/share/zoneinfo/Asia
exit
docker cp /usr/share/zoneinfo/Asia/Shanghai 容器ID或容器名:/usr/share/zoneinfo/Asia
docker exec -it -u root 容器名或id /bin/sh
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```