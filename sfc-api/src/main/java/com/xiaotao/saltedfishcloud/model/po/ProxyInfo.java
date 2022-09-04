package com.xiaotao.saltedfishcloud.model.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProxyInfo {
    @NotEmpty
    private String name;
    @NotNull
    private Type type;
    @NotEmpty
    private String address;
    @Max(65535)
    @Min(1)
    private Integer port;

    public enum Type {
        SOCKS, HTTP
    }

    public Proxy toProxy() {
        return new Proxy(type == Type.SOCKS ? Proxy.Type.SOCKS : Proxy.Type.HTTP, new InetSocketAddress(address, port));
    }
}
