package cn.edu.jxust.arrangeproduce.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author ZSS
 * @description 通用类 保证序列化json的时候，如果是null的对象，key会消失
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * json序列化的时候忽略此方法，不再序列化至结果当中
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * 返回成功
     *
     * @param <T> 泛型
     * @return 泛型
     */
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    /**
     * 返回成功 自定义返回信息
     *
     * @param msg 返回信息
     * @param <T> 泛型
     * @return ServerResponse
     */
    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 返回成功 自定义返回数据
     *
     * @param data 返回数据
     * @param <T>  泛型
     * @return ServerResponse
     */
    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    /**
     * 返回成功， 自定义返回信息和返回数据
     *
     * @param msg  返回信息
     * @param data 返回数据
     * @param <T>  泛型
     * @return ServerResponse
     */
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回失败
     *
     * @param <T> 泛型
     * @return ServerResponse
     */
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode());
    }

    /**
     * 返回失败 自定义错误信息
     *
     * @param errprMessage 错误信息
     * @param <T>          泛型
     * @return ServerResponse
     */
    public static <T> ServerResponse<T> createByErrorMessage(String errprMessage) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errprMessage);
    }

    /**
     * 返回失败 自定义错误编码和错误信息
     *
     * @param errorCode    错误编码
     * @param errorMessage 错误信息
     * @param <T>          泛型
     * @return ServerResponse
     */
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<>(errorCode, errorMessage);
    }

}
