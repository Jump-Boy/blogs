package com.hmh.spring.boot.blog.vo;

import java.io.Serializable;

/**
 * Tag 值对象
 *
 * @author hmh
 * @date 2019/3/12
 */
public class TagVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 标签名
     */
    private String name;
    /**
     * 该标签的数量（用于统计热门）
     */
    private Long count;

    public TagVO(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
