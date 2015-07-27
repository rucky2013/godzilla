package cn.godzilla.service;

import java.util.List;

import cn.godzilla.common.Constant;
import cn.godzilla.model.FunRight;

public interface FunRightService extends Constant{

	List<FunRight> findFunRightsByUsername(String username);
	
}
