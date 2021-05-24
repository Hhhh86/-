package com.zhiwen.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 首页类型排行榜
 * @date : 2020/12/1 09:55
 */
@Data
@Builder
public class TopType {
    /**
     * 类型id
     */
    private Integer typeId;

    /**
     * 类型名
     */
    private String typeName;
    /**
     * 类型下对应的博客数目
     */
    private Integer blogNums;
}
