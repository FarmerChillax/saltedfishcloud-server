package com.sfc.rpc;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public interface RPCManager {
    /**
     * 将一个对象为RPC服务提供者，待注册的对象的类应使用{@link com.sfc.rpc.annotation.RPCService}注解定义作用域，方法使用{@link com.sfc.rpc.annotation.RPCAction}作为响应方法。
     * @param obj   待注册的对象
     */
    void registerRPCService(Object obj);

    /**
     * 获取注册的RPC服务，对该服务的调用会直接发起对应的RPC调用
     * @param clazz     RPC服务类
     * @param <T>       RPC服务类类型
     * @return          RPC服务类的代理类
     */
    <T> T getRPCService(Class<T> clazz);

    /**
     * 对所有节点发起RPC请求，接收多个响应
     * @param request 请求参数
     * @param resultType 响应结果数据类型
     */
    <T> List<RPCResponse<T>> callAll(RPCRequest request, Class<T> resultType) throws IOException;

    /**
     * 对所有节点发起RPC请求，接收多个响应
     * @param request 请求参数
     * @param timeout 等待超时
     * @param resultType 响应结果数据类型
     */
    <T> List<RPCResponse<T>> callAll(RPCRequest request, Class<T> resultType, Duration timeout) throws IOException;

    /**
     * 发起RPC请求
     * @param request 请求参数
     * @param timeout 等待超时
     * @param resultType 响应结果数据类型
     */
    <T> RPCResponse<T> call(RPCRequest request, Class<T> resultType, Duration timeout) throws IOException;

    /**
     * 发起RPC请求，默认2分钟超时
     * @param request 请求参数
     * @param resultType 响应结果数据类型
     */
    <T> RPCResponse<T> call(RPCRequest request, Class<T> resultType) throws IOException;


    /**
     * 发起RPC请求，默认2分钟超时，不处理响应结果数据
     * @param request 请求参数
     */
    <T> RPCResponse<T> call(RPCRequest request) throws IOException;

    /**
     * 注册RPC请求处理器
     *
     * @param functionName 函数名称
     * @param handler      操作器
     */
    <T> void registerRpcHandler(String functionName, RPCHandler<T> handler);
}
