## 下载
- [THUDM/codegeex2-6b-int4 · Hugging Face](https://huggingface.co/THUDM/codegeex2-6b-int4) 
- `conda create -n codeGeeX2 python=3.9 -y`
- `conda install notebook`
- `pip install protobuf transformers==4.30.2 cpm_kernels torch>=2.0 gradio mdtex2html sentencepiece accelerate`
- 下载并加载模型（`THUDM/codegeex2-6b-int4`可以使用本地下载的模型路径替换）
```python
from transformers import AutoTokenizer, AutoModel
tokenizer = AutoTokenizer.from_pretrained("THUDM/codegeex2-6b-int4", trust_remote_code=True)
model = AutoModel.from_pretrained("THUDM/codegeex2-6b-int4", trust_remote_code=True, device='cuda')
model = model.eval()

prompt = "# language: Python\n# write a bubble sort function\n"
inputs = tokenizer.encode(prompt, return_tensors="pt").to(model.device)
outputs = model.generate(inputs, max_length=256, top_k=1)
response = tokenizer.decode(outputs[0])

print(response)
```
