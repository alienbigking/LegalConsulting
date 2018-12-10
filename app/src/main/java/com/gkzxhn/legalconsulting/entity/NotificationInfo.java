package com.gkzxhn.legalconsulting.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @classname：NotificationInfo
 * @author：liushaoxiang
 * @date：2018/12/4 5:33 PM
 * @description：
 */
@Entity
public class NotificationInfo {

    @Id(autoincrement = true)
    private Long id;
    private String sessionId;
    private String fromAccount;
    private long time;
    private String content;
    @Generated(hash = 2005230866)
    public NotificationInfo(Long id, String sessionId, String fromAccount,
            long time, String content) {
        this.id = id;
        this.sessionId = sessionId;
        this.fromAccount = fromAccount;
        this.time = time;
        this.content = content;
    }
    @Generated(hash = 273180940)
    public NotificationInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getFromAccount() {
        return this.fromAccount;
    }
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
