package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-20 17:29:27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TwitterDTO {

	Long id;
	String name;
	Integer version;
}