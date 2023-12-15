package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.DeptMapper;
import com.yt.app.api.v1.mapper.PostMapper;
import com.yt.app.api.v1.service.PostService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Post;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

@Service
public class PostServiceImpl extends YtBaseServiceImpl<Post, Long> implements PostService {
	@Autowired
	private PostMapper mapper;

	@Autowired
	private DeptMapper deptmapper;

	@Override
	@Transactional
	public Integer post(Post t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Post> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Post> list = mapper.list(param);
		list.forEach(p -> {
			p.setDeptname(deptmapper.get(p.getDept_id()).getName());
		});
		return new YtPageBean<Post>(param, list, count);
	}

	@Override
	public Post get(Long id) {
		Post t = mapper.get(id);
		return t;
	}
}