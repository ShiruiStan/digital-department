package com.atcdi.digital.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiDescriptionBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class ApiLoginDocConfig implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        Operation loginOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .tags(Set.of("登录退出接口"))
                .uniqueId("loginUsingPOST")
                .summary("登录")
                .consumes(Set.of(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .produces(Set.of(MediaType.APPLICATION_JSON_VALUE))
                .requestParameters(Arrays.asList(
                        new RequestParameterBuilder()
                                .description("用户名")
                                .name("username")
                                .required(true)
                                .build(),
                        new RequestParameterBuilder()
                                .description("密码")
                                .name("password")
                                .required(true)
                                .build()
                ))
                .build();
        Operation logoutOperation =  new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.GET)
                .tags(Set.of("登录退出接口"))
                .uniqueId("logoutUsingGET")
                .summary("退出")
                .produces(Set.of(MediaType.APPLICATION_JSON_VALUE))
                .build();

        ApiDescription loginApiDescription = new ApiDescription("", "/login", "", "", List.of(loginOperation), false);
        ApiDescription logoutApiDescription = new ApiDescription("", "/logout", "", "", List.of(logoutOperation), false);

        return Arrays.asList(loginApiDescription, logoutApiDescription);
    }


    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.OAS_30.equals(documentationType);
    }
}
