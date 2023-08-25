## 安装
- 安装`cuda`
[CUDA Toolkit 11.7 Downloads | NVIDIA Developer](https://developer.nvidia.com/cuda-11-7-0-download-archive?target_os=Windows&target_arch=x86_64&target_version=11&target_type=exe_local)
- 安装`conda`
	- `conda config --set proxy_servers.http http://127.0.0.1:7890`
	- `conda config --set proxy_servers.https http://127.0.0.1:7890`
	- 不要用台湾的代理
- 安装pytorch
[Start Locally | PyTorch](https://pytorch.org/get-started/locally/)
`conda install pytorch torchvision torchaudio pytorch-cuda=11.7 -c pytorch -c nvidia`
- 验证
进入python环境，输入
```
import torch
x = torch.rand(5, 3)
print(x)
```
输出类似：
```
tensor([[0.3380, 0.3845, 0.3217],
        [0.8337, 0.9050, 0.2650],
        [0.2979, 0.7141, 0.9069],
        [0.1449, 0.1132, 0.1375],
        [0.4675, 0.3947, 0.1426]])
```
验证cuda可用性
```
torch.cuda.is_available()
```