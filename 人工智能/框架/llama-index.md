### data
```python
documents = SimpleDirectoryReader("./data", file_metadata=filename_fn, filename_as_id=True).load_data()
```

### llm
```python
from llama_index import ServiceContext, set_global_service_context
from llama_index.embeddings import HuggingFaceEmbedding
from llama_index.llms.openai_like import OpenAILike

QWEN_72B = "Qwen-72B-Chat"
llm = OpenAILike(api_key="EMPTY", api_base="http://10.xxx/v1", model=QWEN_72B, is_chat_model=True)

message = ChatMessage(**{"role": "user", "content": "你好"})
llm.chat([message]).message.content
llm.complete("你好")
```

### 向量化模型
```python
from llama_index.embeddings import HuggingFaceEmbedding

BGE_LARGE_ZH = "./bge-large-zh-v1.5"
embed_model = HuggingFaceEmbedding(model_name=BGE_LARGE_ZH, trust_remote_code=True)
```

### doc
```python
docs = []
doc = Document(text="text",
     metadata={
       "input_param": "param", 
       "output_param": "param22"
     },
     excluded_llm_metadata_keys=["input_param", "output_param"],
     text_template="{content}")
docs.append(doc)
```

### index
```python
index = VectorStoreIndex.from_documents(docs, service_context=service_context)
```
持久化
```
import os.path
from llama_index import (
    VectorStoreIndex,
    SimpleDirectoryReader,
    StorageContext,
    load_index_from_storage,
)

# check if storage already exists
PERSIST_DIR = "storage"
if not os.path.exists(PERSIST_DIR):
    # load the documents and create the index
    documents = SimpleDirectoryReader("data").load_data()
    index = VectorStoreIndex.from_documents(documents)
    # store it for later
    index.storage_context.persist(persist_dir=PERSIST_DIR)
else:
    # load the existing index
    storage_context = StorageContext.from_defaults(persist_dir=PERSIST_DIR)
    index = load_index_from_storage(storage_context)
```

### query_engine
```
index = VectorStoreIndex.from_documents(docs, service_context=service_context)
query_engine = index.as_query_engine()
response = query_engine.query("中兴通讯是一家什么公司")
```

### retriever
```python
service_context = ServiceContext.from_defaults(embed_model=embed_model, llm=None)
index = VectorStoreIndex.from_documents(docs, service_context=service_context)
retriever = index.as_retriever(similarity_top_k=10)
[node.text for node in retriever.retrieve("要查询")]
```

### format_output
```python
from llama_index.output_parsers import PydanticOutputParser
from llama_index.program import LLMTextCompletionProgram

def build_pydantic_parser(cls):
    return PydanticOutputParser(
        output_cls=cls,
        pydantic_format_tmpl="""
下面是JSON的结构schema：
{schema}

输出一个有效的json对象，但不要重复schema。
""")

class Steps(BaseModel):
    steps: list[Step]

LLMTextCompletionProgram.from_defaults(
        output_cls=Steps,
  output_parser=build_pydantic_parser(Steps),
        prompt_template_str=prompt_content,
        verbose=True,
        llm=qwen_llm
    )()
```

### agent
```python
from llama_index.tools import FunctionTool
from llama_index.agent import ReActAgent


# define sample Tool
def multiply(a: int, b: int) -> int:
    # """Multiply two integers and returns the result integer"""
    return a * b

multiply_tool = FunctionTool.from_defaults(fn=multiply)

def add(a: int, b: int) -> int:
    """Add two integers and returns the result integer"""
    return a + b

add_tool = FunctionTool.from_defaults(fn=add)

agent = ReActAgent.from_tools([multiply_tool, add_tool], llm=llm, verbose=True)
agent.chat("2.0+6.8")
```

