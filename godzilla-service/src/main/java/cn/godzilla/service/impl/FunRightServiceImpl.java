package cn.godzilla.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.dao.FunRightMapper;
import cn.godzilla.model.FunRight;
import cn.godzilla.service.FunRightService;

@Service("funRigthService")
public class FunRightServiceImpl implements FunRightService {

	@Autowired
	private FunRightMapper dao;

	@Override
	public List<FunRight> findFunRightsByUsername(String username) {
		List<FunRight> funRightList = dao.queryRightsByUsername(username);
		return funRightList;
	}

}
