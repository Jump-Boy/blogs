package com.hmh.spring.boot.blog.vo;

import com.hmh.spring.boot.blog.domain.Catalog;

import java.io.Serializable;

/**
 * Catalog视图层实体
 *
 * @author hmh
 * @date 2019/3/12
 */
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private Catalog catalog;

    public CatalogVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

}
