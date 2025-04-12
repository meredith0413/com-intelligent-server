package com.intelligent.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aliyun.oss")
@Configuration
@Data
public class OssConfig {

    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;


    @Bean
    public OSS ossClient() {
        // 创建OSSClient实例。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        // 创建ClientBuilderConfiguration。
        // ClientBuilderConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
//        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
//        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
//        conf.setSocketTimeout(10000);
//        // 设置建立连接的超时时间，默认为50000毫秒。
//        conf.setConnectionTimeout(10000);
//        // 设置失败请求重试次数，默认为3次。
//        conf.setMaxErrorRetry(5);
        OSS ossClient = new OSSClientBuilder().build(endPoint, credentialsProvider);
        return ossClient;
    }
}
