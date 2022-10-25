package org.ivipi.ratel.common.dto;

import lombok.Data;

@Data
public class PageDto {
    private  static final long serialVersionUID = 1L;


    private static final int MAX_PAGE_NUM = 1000;
    private static final int MAX_PAGE_SIZE = 200;

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;


    public PageDto() {
        this.pageNum = DEFAULT_PAGE_NUM;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    private Integer pageNum;

    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = (pageNum == null || pageNum > MAX_PAGE_NUM) ? DEFAULT_PAGE_NUM : pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = (pageSize == null || pageSize > MAX_PAGE_SIZE) ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
