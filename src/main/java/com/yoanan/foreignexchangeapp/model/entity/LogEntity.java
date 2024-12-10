package com.yoanan.foreignexchangeapp.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name="log_errors")
public class LogEntity {

    private String id;
    private String requestUrl;
    private LocalDateTime dateTime;
    private String methodName;
    private String className;
    private String exceptionMessage;

    public LogEntity() {
    }

    public LogEntity(String requestUrl, LocalDateTime dateTime, String methodName, String className, String exceptionMessage) {
        this.requestUrl = requestUrl;
        this.dateTime = dateTime;
        this.methodName = methodName;
        this.className = className;
        this.exceptionMessage = exceptionMessage;
    }

    @Id
    @GeneratedValue(generator = "uuid-string")
    @GenericGenerator(name="uuid-string",strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id", nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    public LogEntity setId(String id) {
        this.id = id;
        return this;
    }

    @Column(name="request_url")
    public String getRequestUrl() {
        return requestUrl;
    }

    public LogEntity setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    @Column(name="date_time")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LogEntity setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    @Column(name="method_name")
    public String getMethodName() {
        return methodName;
    }

    public LogEntity setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Column(name="class_name")
    public String getClassName() {
        return className;
    }

    public LogEntity setClassName(String className) {
        this.className = className;
        return this;
    }

    @Column(name="exeption_message")
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public LogEntity setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
        return this;
    }
}
