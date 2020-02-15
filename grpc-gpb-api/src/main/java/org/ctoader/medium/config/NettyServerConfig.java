package org.ctoader.medium.config;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class NettyServerConfig {

    @Value("${server.port:9111}")
    private Integer port;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(destroyMethod = "shutdown")
    public Server nettyServer() throws IOException {
        log.info("Creating netty server for port {}.", this.port);

        NettyServerBuilder nettyServerBuilder = NettyServerBuilder.forPort(port);

        applicationContext.getBeansOfType(BindableService.class)
                .values()
                .stream()
                .forEach(nettyServerBuilder::addService);

        Server nettyServer = nettyServerBuilder.build();
        nettyServer.start();
        log.info("Netty server successfully started on port {}.", this.port);

        return nettyServer;
    }
}
