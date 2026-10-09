package org.dromara.nail.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 业绩录入时会员校验结果
 */
@Data
public class NailPerformanceMemberCheckVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean member;

    private Long memberId;

    private String memberNo;

    private String name;

    private String phone;
}
