package cn.godzilla.dao;

import java.util.List;
import java.util.Map;

import cn.godzilla.model.PropBill;

public interface PropBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PropBill record);

    int insertSelective(PropBill record);

    PropBill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PropBill record);

    int updateByPrimaryKey(PropBill record);

	List<PropBill> queryPropBillByProjectCodeAndStatus(Map<String, Object> parameterMap);

	int updatePropBillById(Map<String, Object> parameterMap);

}