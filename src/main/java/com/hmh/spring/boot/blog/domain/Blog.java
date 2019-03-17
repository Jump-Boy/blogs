package com.hmh.spring.boot.blog.domain;

import com.github.rjeschke.txtmark.Processor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 博客实体
 *
 * @author hmh
 * @date 2019/3/11
 */
@Entity
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识（主键，自增长策略）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 博客标题（映射为字段，值不能为空）
     */
    @NotEmpty(message = "标题不能为空")
    @Size(min=2, max=50)
    @Column(nullable = false, length = 50)
    private String title;

    /**
     * 博客摘要（映射为字段，值不能为空）
     */
    @NotEmpty(message = "摘要不能为空")
    @Size(min=2, max=300)
    @Column(nullable = false)
    private String summary;

    /**
     * 博客内容（Markdown格式，@Lob：大对象，映射 MySQL 的 Long Text 类型，@Basic：懒加载）
     */
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String content;

    /**
     * 博客内容（html格式，）
     */
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String htmlContent;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    /**
     * 创建时间（由数据库自动创建时间）
     */
    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    /**
     * 访问量、阅读量
     */
    @Column(name="readSize")
    private Long readSize = 0L;

    /**
     * 评论量
     */
    @Column(name="commentSize")
    private Long commentSize = 0L;

    /**
     * 点赞量
     */
    @Column(name="voteSize")
    private Long voteSize = 0L;

    /**
     * 评论（一篇博客可能有多条评论）
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    /**
     * 点赞（一篇博客可能有多条点赞）
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
    private List<Vote> votes;

    /**
     * 分类（一个博客只能有一种类别，博客的归类）
     */
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;

    /**
     * 标签
     */
    @Column(name="tags", length = 100)
    private String tags;

    protected Blog() {
    }

    public Blog(String title, String summary,String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        //通过依赖的Markdown jar工具来讲Markdown格式文件转成html文件
        this.htmlContent = Processor.process(content);
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

    public String getHtmlContent() {
        return htmlContent;
    }

    public Long getReadSize() {
        return readSize;
    }

    public void setReadSize(Long readSize) {
        this.readSize = readSize;
    }

    public Long getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Long commentSize) {
        this.commentSize = commentSize;
    }

    public Long getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Long voteSize) {
        this.voteSize = voteSize;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize = (long)this.comments.size();
    }

    /**
     * 添加评论（注意评论相关操作，除了影响comments字段，可能还影响commentSize字段）
     *
     * @author hmh
     * @date 2019/3/12
     * @param comment
     * @return void
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.commentSize = (long)this.comments.size();
    }

    /**
     * 删除某条评论
     *
     * @author hmh
     * @date 2019/3/12
     * @param commentId 评论id
     * @return void
     */
    public void removeComment(Long commentId) {
        for (int index=0; index < this.comments.size(); index ++ ) {
            //包装类型间的相等判断应该用equals，而不是'=='
            if (comments.get(index).getId().equals(commentId)) {
                this.comments.remove(index);
                break;
            }
        }
        this.commentSize = (long)this.comments.size();
    }

    /**
     * 添加点赞（添加点赞除了影响votes，还会影响voteSize字段）
     *
     * @author hmh
     * @date 2019/3/12
     * @param vote
     * @return boolean
     */
    public boolean addVote(Vote vote) {
        boolean isExist = false;
        // 判断是否是重复点赞
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getUser().getId().equals(vote.getUser().getId())) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            this.votes.add(vote);
            this.voteSize = (long)this.votes.size();
        }
        return isExist;
    }

    /**
     * 取消点赞
     *
     * @author hmh
     * @date 2019/3/12
     * @param voteId
     * @return void
     */
    public void removeVote(Long voteId) {
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getId().equals(voteId)) {
                this.votes.remove(index);
                break;
            }
        }
        //更新点赞数
        this.voteSize = (long)this.votes.size();
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.voteSize = (long)this.votes.size();
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

}
