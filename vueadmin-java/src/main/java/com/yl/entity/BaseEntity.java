package com.yl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yl.common.JsonLongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

//	@TableId(value = "id", type = IdType.AUTO)
	@JsonSerialize(using = JsonLongSerializer.class )		//Long类型长度过长，导致精度丢失方案
	private Long id;

	private LocalDateTime created;
	private LocalDateTime updated;

	private Integer statu;
}
