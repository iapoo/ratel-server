package org.ivipa.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipa.ratel.rockie.common.model.Team;
import org.ivipa.ratel.rockie.domain.entity.TeamDo;

import java.util.List;

public interface TeamMapper extends BaseMapper<TeamDo> {
    List<Team> getTeams(IPage<Team> page, @Param("customerId") Long customerId, @Param("teamName") String teamName);
}
