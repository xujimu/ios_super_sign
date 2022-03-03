package com.wlznsb.iossupersign.execption;


import lombok.*;

/**
 * 其他状态码异常
 *
 * @Author: xjm
 * @Date: 2021/09/15/18:47
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResRunException extends RuntimeException{

    private Integer code;
    private String message;
    private Object data ;

}
