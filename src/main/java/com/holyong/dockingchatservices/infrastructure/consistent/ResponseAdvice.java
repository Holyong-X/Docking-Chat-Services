package com.holyong.dockingchatservices.infrastructure.consistent;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {


    @Autowired
    ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //String类型判断  SpringBoot自动帮我们实现包装类的封装 自动转换成json
        if (body instanceof String) {
            return objectMapper.writeValueAsString(CorrespondingResult.success(CorrespondingCode.RC100.getMessage(), body));
        }
        return CorrespondingResult.success(CorrespondingCode.RC100.getMessage(), body);
    }
}
