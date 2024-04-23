package com.xhf.pc.gateway.filter;

import com.google.gson.Gson;
import com.xhf.model.user.vo.LoginVo;
import com.xhf.pc.gateway.redis.CacheService;
import com.xhf.pc.gateway.redis.utils.RedisConstant;
import com.xhf.utils.common.JWTUtils;
import com.xhf.utils.common.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private Gson gson;

    @Value("${test.array1:}")
    private String[] testArray1;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 前置记录日志
        long time = recordBefore(exchange, chain);
        // 权限校验
        return (Mono<Void>) permissionCheck(exchange, chain, time);
    }

    /**
     * 权限校验
     * @param exchange
     * @param chain
     * @param time
     */
    private Mono<? extends Object> permissionCheck(ServerWebExchange exchange, GatewayFilterChain chain, long time) {
        // 获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory dataBufferFactory = originalResponse.bufferFactory();

        HttpStatus statusCode = originalResponse.getStatusCode();

        // 接口放行
        boolean flag = interfaceRelease(request);

        // 判断是否是登录请求
        if (flag) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                recordAfter(time);
            }));
        }

        if (statusCode == HttpStatus.OK) {
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 等调用完转发的接口后才会执行
                @Override
                public Mono<Void>  writeWith(Publisher<? extends DataBuffer> body) {
                    log.info("body instanceof Flux: {}", (body instanceof Flux));
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 往返回值里写数据
                        // 拼接字符串
                        return super.writeWith(
                                fluxBody.map(dataBuffer -> {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    //释放掉内存
                                    DataBufferUtils.release(dataBuffer);
                                    // 构建日志
                                    StringBuilder sb2 = new StringBuilder(200);
                                    List<Object> rspArgs = new ArrayList<>();
                                    rspArgs.add(originalResponse.getStatusCode());
                                    //data
                                    String data = new String(content, StandardCharsets.UTF_8);
                                    sb2.append(data);
                                    // 打印日志
                                    log.info("响应结果：" + data);
                                    return dataBufferFactory.wrap(content);
                                }));
                    } else {
                        // 8. 调用失败，返回一个规范的错误码
                        log.error("<--- {} 响应code异常", getStatusCode());
                    }
                    recordAfter(time);
                    return super.writeWith(body);
                }
            };

            // 判断是否存在token
            String authorization = request.getHeaders().getFirst("Authorization");
            // 没有权限
            if (StringUtils.isBlank(authorization)) {
                return unauthorizedHandler(originalResponse, time, "token 不存在");
            }
            // 提取token, 去除Bear
            String token = authorization.substring(7);
            log.info("token {}", token);

            // 判断token是否有效
            boolean verify = JWTUtils.verify(token);
            if (!verify) {
                return unauthorizedHandler(originalResponse, time, "token 无效");
            }
            // 判断token是否过期, redis
            try {
                Long userId = JWTUtils.getUserId(token);
                String json = cacheService.get(RedisConstant.USER + userId);
                String accessToken = gson.fromJson(json, LoginVo.class).getAccessToken();
                if (StringUtils.isBlank(accessToken) || !accessToken.equals(token)) {
                    return unauthorizedHandler(originalResponse, time, "token 过期");
                }
            }catch (Exception e) {
                log.info("", e);
                return unauthorizedHandler(originalResponse, time, "token 过期");
            }

            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            recordAfter(time);
        }));
    }

    /**
     * 判断接口是否放行
     * @param request
     * @return
     */
    private boolean interfaceRelease(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        for (int i = 0; i < testArray1.length; i++) {
            if (path.contains(testArray1[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理无权限异常
     * @param originalResponse
     * @param time
     * @return
     */
    private Mono<? extends Object> unauthorizedHandler(ServerHttpResponse originalResponse, long time, String logInfo) {
        originalResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        log.error(logInfo);
        recordAfter(time);
        return originalResponse.setComplete();
    }

    /**
     * 后置记录日志
     * @param time
     */
    private void recordAfter(long time) {
        log.info("executed time : ", (System.currentTimeMillis() - time));
        log.info("================log    end================");
    }

    /**
     * 前置记录日志
     * @param exchange
     * @param chain
     */
    private long recordBefore(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("=====================log start================================");
        ServerHttpRequest request = exchange.getRequest();
        // 参数打印
        MultiValueMap<String, String> params = request.getQueryParams();
        log.info("params: " + params);
        // 打印ip
        InetSocketAddress address = request.getRemoteAddress();
        log.info("address: " + address.toString());
        return System.currentTimeMillis();
    }

    private class ResponseWrapper {
        private Object responseBody;

        public Object getResponseBody() {
            return responseBody;
        }

        public void setResponseBody(Object responseBody) {
            this.responseBody = responseBody;
        }
    }


    @Override
    public int getOrder() {
        return -2;
    }
}
