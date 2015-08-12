package cn.godzilla.service;

import cn.godzilla.common.ReturnCodeEnum;

public interface SvnService {

	ReturnCodeEnum svnCommit(String projectCode, String profile);

}
