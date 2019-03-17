package com.hmh.spring.boot.blog.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 点赞实体
 *
 * @author hmh
 * @date 2019/3/12
 */
@Entity
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识（主键，自增长策略）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 外键user_id（点赞者）
     */
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    /**
     * 记录创建时间（映射为字段，值不能为空，由数据库自动创建时间）
     */
    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    protected Vote() {
    }

    public Vote(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

}
