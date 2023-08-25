# EventSource

Eventsource可以在建立连接后，保持连接。但只能由后端向前端推送消息

如果eventsource中的消息要经过nginx转发，可以在nginx增加eventsource相关配置，也可以直接在后端响应中，增加值为no的X-accel-Buffering响应头。

[Module ngx_http_proxy_module](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_buffer_size)