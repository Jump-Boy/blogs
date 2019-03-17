package com.hmh.spring.boot.blog.util;


import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ConstraintViolationException处理器，用于对捕获的约束校验异常进行处理
 *
 * @author hmh
 * @date 2019/3/6
 */
public class ConstraintViolationExceptionHandler {

    /**
     * 获取格式化后的ConstraintViolationException信息
     *
     * @author hmh
     * @date 2019/3/6
     * @param e 捕获的ConstraintViolationException
     * @return java.lang.String
     */
    public static String getMessage(ConstraintViolationException e) {
        List<String> msgList = new LinkedList<>();
        /*
         * ConstraintViolationException中包含了可能多个约束校验异常，如邮箱参数为空的情况下，@NotEmpty和@Email都会抛出异常。
         * 所以这里获取所有异常（得到的是一个Set<ConstraintViolation<?>>），然后遍历取出message，最终将所有message转成以“;”
         * 为分隔符的字串返回。
         */
        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }
        String message = StringUtils.join(msgList.toArray(), ";");
        return message;
    }

}
